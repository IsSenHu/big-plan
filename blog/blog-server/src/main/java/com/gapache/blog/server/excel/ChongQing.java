package com.gapache.blog.server.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author HuSen
 * @since 2020/8/19 4:59 下午
 */
public class ChongQing {

    private static final RestTemplate REST = new RestTemplate();
    private static final String KEY = "f9d4b6f76fd3142fadc327ce483b0333";
    private static final String CITY = "成都";
    private static final String PLACE_TEXT =
            "https://restapi.amap.com/v3/place/text?key=KEYKEY&keywords=KEYWORDS&city=CITY&citylimit=true&output=JSON&page=PAGE";
    private static final String PLACE_AROUND =
            "https://restapi.amap.com/v3/place/around?location=LOCATION&key=KEYKEY&keywords=超市|便利店&city=CITY&output=JSON&page=PAGE";
    private static final int SIZE = 1000;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE = 5;
    private static final int SHARD_SIZE = 20;

    public static void main(String[] args) {
        try {
            exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exec() throws IOException {
        FileInputStream inputStream = new FileInputStream("/Users/husen/data/cd/cd.json");
        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
        String data = new String(bytes, StandardCharsets.UTF_8);
        List<Community> communities = JSONArray.parseArray(data, Community.class);
        int total = communities.size();
        System.out.println("total:" + total);
        int batch = total % SIZE == 0 ? total / SIZE : total / SIZE + 1;

        for (int i = 2; i < 4; i++) {
            List<Community> per = new ArrayList<>(SIZE);
            int m = total - i * SIZE;
            m = Math.min(m, 1000);
            for (int j = 0; j < m; j++) {
                per.add(communities.get(i * SIZE + j));
            }
            // 提交任务
            try {
                submit(per, null, i);
            } catch (Exception e) {
                FileWriter ew = new FileWriter("/Users/husen/data/cd/batchError" + batch + ".json");
                ew.write(JSONObject.toJSONString(per));
                ew.flush();
                ew.close();
            }
        }
    }

    public static void submit(final List<Community> task, CountDownLatch countDownLatch, final int batch) {
        final int taskSize = task.size();
        int shards = taskSize % SHARD_SIZE == 0 ? taskSize / SHARD_SIZE : taskSize / SHARD_SIZE + 1;

        for (int i = 0; i < shards; i++) {
            final FileWriter errorWriter;
            final FileWriter successWriter;
            try {
                String index = batch + "-" + i;
                errorWriter = new FileWriter("/Users/husen/data/cd/error" + index + ".json");
                successWriter = new FileWriter("/Users/husen/data/cd/success" + index + ".json");
            } catch (Exception e) {
                System.out.println("第:" + batch + "." + shards + "批失败");
                return;
            }

            int start = i * SHARD_SIZE;
            List<Community> error = new ArrayList<>();
            List<Community> found = new ArrayList<>();
            List<Shop> shops = new ArrayList<>();

            queryCommunity(task, start, error, found);
            queryShop(error, found, shops);

            try {
                errorWriter.write(JSONObject.toJSONString(error));
                errorWriter.flush();
                errorWriter.close();

                successWriter.write(JSONObject.toJSONString(shops));
                successWriter.flush();
                successWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    protected static void queryShop(List<Community> error, List<Community> found, List<Shop> shops) {
        for (Community community : found) {
            String restForObject = null;
            int page = 1;
            int totalPage = 0;

            while (restForObject == null || page < totalPage) {
                String replace = PLACE_AROUND.replace("KEYKEY", KEY)
                        .replace("CITY", CITY)
                        .replace("LOCATION", community.getLocation())
                        .replace("PAGE", String.valueOf(page));

                restForObject = restQuery(replace);
                if (restForObject == null) {
                    error.add(community);
                    break;
                }

                JSONObject object = JSONObject.parseObject(restForObject);
                if (!"1".equals(object.getString("status"))) {
                    error.add(community);
                    break;
                }

                if (totalPage == 0) {
                    totalPage = getTotalPage(object);
                }

                JSONArray storePois = object.getJSONArray("pois");
                int size = storePois.size();
                for (int y = 0; y < size; y++) {
                    JSONObject store = storePois.getJSONObject(y);
                    Shop shop = new Shop();
                    shop.setCommunity(community.getPName());
                    shop.setName(community.getName());
                    shop.setStoreName(getValue(store, "name"));
                    shop.setUid(getValue(store, "id"));
                    shop.setTelephone(getValue(store, "tel"));
                    shop.setAddress(getValue(store, "address"));
                    String distance = getValue(store, "distance");
                    shop.setDistance(distance == null ? null : Integer.parseInt(distance));
                    shop.setProvince(getValue(store, "pname"));
                    shop.setCity(getValue(store, "cityname"));
                    shop.setArea(getValue(store, "adname"));
                    shop.setTag(getValue(store, "typecode"));
                    shop.setType(getValue(store, "type"));
                    shops.add(shop);
                    System.out.println(shop.getStoreName());
                }
                page++;
            }
        }
    }

    public static void queryCommunity(List<Community> task, int start, List<Community> error, List<Community> found) {
        for (int j = 0; j < Math.min(SHARD_SIZE, task.size()); j++) {
            Community community = task.get(start + j);
            System.out.println("do:" + (start + j) + " " + community.getName());
            String forObject = null;
            int current = 1;
            int totalPages = 0;
            while (forObject == null || current < totalPages) {
                String path = PLACE_TEXT.replace("KEYKEY", KEY)
                        .replace("KEYWORDS", community.getName())
                        .replace("CITY", CITY)
                        .replace("PAGE", String.valueOf(current));

                forObject = restQuery(path);
                if (forObject == null) {
                    community.setPName(community.getName());
                    error.add(community);
                    break;
                }

                if (StringUtils.isBlank(forObject)) {
                    community.setPName(community.getName());
                    error.add(community);
                    break;
                }
                JSONObject jsonObject = JSONObject.parseObject(forObject);
                String status = jsonObject.getString("status");
                if (!"1".equals(status)) {
                    community.setPName(community.getName());
                    error.add(community);
                    break;
                }

                if (totalPages == 0) {
                    totalPages = getTotalPage(jsonObject);
                }

                JSONArray pois = jsonObject.getJSONArray("pois");
                int size = pois.size();

                String firstName = "";
                for (int x = 0; x < size; x++) {
                    JSONObject poi = pois.getJSONObject(x);
                    String name = poi.getString("name");
                    Community newFound = null;
                    // 强行匹配第一个
                    if (x == 0) {
                        newFound = new Community();
                        String location = poi.getString("location");
                        newFound.setPName(community.getName());
                        newFound.setName(name);
                        newFound.setLocation(location);
                        firstName = name;
                    } else {
                        // 找到了自己
                        if (StringUtils.equals(name, community.getName())) {
                            newFound = new Community();
                            String location = poi.getString("location");
                            newFound.setPName(community.getName());
                            newFound.setName(name);
                            newFound.setLocation(location);
                        }
                        // 找到了自己的门
                        if (StringUtils.contains(name, community.getName())) {
                            if (name.replace(community.getName(), "").contains("门")) {
                                newFound = new Community();
                                newFound.setPName(community.getName());
                                newFound.setName(name);
                                String location = poi.getString("location");
                                newFound.setLocation(location);
                            }
                        } else if (StringUtils.contains(name, firstName)) {
                            if (name.replace(firstName, "").contains("门")) {
                                newFound = new Community();
                                newFound.setPName(community.getName());
                                newFound.setName(name);
                                String location = poi.getString("location");
                                newFound.setLocation(location);
                            }
                        }
                    }
                    if (newFound != null) {
                        found.add(newFound);
                    }
                }
                current++;
            }
        }
    }

    private static String restQuery(String path) {
        try {
            return REST.getForObject(path, String.class);
        } catch (Exception e) {
            System.err.println("error: " + path);
            return null;
        }
    }

    private static String getValue(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch (Exception e) {
            return null;
        }
    }

    private static int getTotalPage(JSONObject object) {
        int totalPage;
        String count = object.getString("count");
        int countValue = Integer.parseInt(count);
        totalPage = countValue % DEFAULT_PAGE_SIZE == 0 ? countValue / DEFAULT_PAGE_SIZE : countValue / DEFAULT_PAGE_SIZE + 1;
        return Math.min(totalPage, MAX_PAGE);
    }
}

@Data
class Community {
    private String pName;
    private String name;
    private String location;
}

@Data
class TextResp {
    private String status;
    private String info;
    @JSONField(name = "infocode")
    private String infoCode;
    private String count;
    private JSONObject suggestion;
    private List<TextPoi> pois;
}

@Data
class TextPoi {
    private String id;
    @JSONField(name = "childtype")
    private String childType;
    private String name;
    private String type;
    @JSONField(name = "typecode")
    private String typeCode;
    @JSONField(name = "biz_type")
    private List<String> bizType;
    private String address;
    private String location;
    private JSONObject tel;
    @JSONField(name = "pname")
    private String pName;
    @JSONField(name = "cityname")
    private String cityName;
    @JSONField(name = "adname")
    private String adName;
    @JSONField(name = "shopinfo")
    private String shopInfo;
    private JSONObject distance;
    @JSONField(name = "biz_ext")
    private JSONObject bizExt;
}

@Data
class Shop {
    @ExcelProperty("小区名字")
    private String community;
    @ExcelProperty("小区具体地点")
    private String name;
    @ExcelProperty("超市名称")
    private String storeName;
    @ExcelProperty("唯一ID")
    private String uid;
    @ExcelProperty("电话")
    private String telephone;
    @ExcelProperty("地址")
    private String address;
    @ExcelProperty("距离")
    private Integer distance;
    @ExcelProperty("省")
    private String province;
    @ExcelProperty("市")
    private String city;
    @ExcelProperty("区")
    private String area;
    @ExcelProperty("标签")
    private String tag;
    @ExcelProperty("类型")
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shop shop = (Shop) o;
        return Objects.equals(storeName, shop.storeName) &&
                Objects.equals(uid, shop.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName, uid);
    }
}
