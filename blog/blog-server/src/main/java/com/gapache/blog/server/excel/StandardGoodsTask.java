package com.gapache.blog.server.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @since 2020/9/8 2:28 下午
 */
@Slf4j
@Component
public class StandardGoodsTask {

    private final RestHighLevelClient restHighLevelClient;

    public StandardGoodsTask(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void export() throws IOException {
        JSONArray objects = JSONArray.parseArray("[\n" +
                "  {\"name\":\"异常商品\",\"id\":\"100039\",\"data\":[]},\n" +
                "  {\"name\":\"自定义-热门商品\",\"id\":\"100040\",\"data\":[]},\n" +
                "  {\"name\":\"茶水饮料-饮用水\",\"id\":\"100041\",\"data\":[]},\n" +
                "  {\"name\":\"茶水饮料-碳酸饮料\",\"id\":\"100042\",\"data\":[]},\n" +
                "  {\"name\":\"茶水饮料-果味水\",\"id\":\"100043\",\"data\":[]},\n" +
                "  {\"name\":\"茶水饮料-咖啡茶饮\",\"id\":\"100044\",\"data\":[\"咖啡\"]},\n" +
                "  {\"name\":\"茶水饮料-功能运动水\",\"id\":\"100045\",\"data\":[]},\n" +
                "  {\"name\":\"休闲零食-干果甜食\",\"id\":\"100046\",\"data\":[]},\n" +
                "  {\"name\":\"休闲零食-饼干威化\",\"id\":\"100047\",\"data\":[\"饼干\",\"威化\"]},\n" +
                "  {\"name\":\"休闲零食-膨化食品\",\"id\":\"100048\",\"data\":[]},\n" +
                "  {\"name\":\"休闲零食-麻辣零食\",\"id\":\"100049\",\"data\":[\"辣条\"]},\n" +
                "  {\"name\":\"方便速食-方便面/粉丝\",\"id\":\"100050\",\"data\":[\"方便面\",\"泡面\"]},\n" +
                "  {\"name\":\"方便速食-面包蛋糕\",\"id\":\"100051\",\"data\":[\"面包\"]},\n" +
                "  {\"name\":\"方便速食-罐头酱菜\",\"id\":\"100052\",\"data\":[]},\n" +
                "  {\"name\":\"方便速食-肉制熟食\",\"id\":\"100053\",\"data\":[\"火腿肠\"]},\n" +
                "  {\"name\":\"方便速食-粥类速食\",\"id\":\"100054\",\"data\":[\"八宝粥\"]},\n" +
                "  {\"name\":\"奶制品-常温牛奶\",\"id\":\"100055\",\"data\":[]},\n" +
                "  {\"name\":\"奶制品-低温酸奶\",\"id\":\"100056\",\"data\":[\"酸奶\"]},\n" +
                "  {\"name\":\"奶制品-奶粉冲调\",\"id\":\"100057\",\"data\":[]},\n" +
                "  {\"name\":\"中外名酒-啤酒\",\"id\":\"100058\",\"data\":[\"啤酒\"]},\n" +
                "  {\"name\":\"中外名酒-白酒\",\"id\":\"100059\",\"data\":[\"白酒\"]},\n" +
                "  {\"name\":\"中外名酒-葡萄酒\",\"id\":\"100060\",\"data\":[\"葡萄酒\"]},\n" +
                "  {\"name\":\"中外名酒-鸡尾果酒\",\"id\":\"100061\",\"data\":[\"鸡尾酒\"]},\n" +
                "  {\"name\":\"粮油调料-大米\",\"id\":\"100062\",\"data\":[\"米\"]},\n" +
                "  {\"name\":\"粮油调料-食用油\",\"id\":\"100063\",\"data\":[\"油\"]},\n" +
                "  {\"name\":\"粮油调料-调味料\",\"id\":\"100064\",\"data\":[]},\n" +
                "  {\"name\":\"粮油调料-杂粮干货\",\"id\":\"100065\",\"data\":[]},\n" +
                "  {\"name\":\"个人护理-洗发沐浴\",\"id\":\"100066\",\"data\":[]},\n" +
                "  {\"name\":\"个人护理-美容洁面\",\"id\":\"100067\",\"data\":[]},\n" +
                "  {\"name\":\"个人护理-口腔护理\",\"id\":\"100068\",\"data\":[\"牙膏\"]},\n" +
                "  {\"name\":\"个人护理-卫生护理\",\"id\":\"100069\",\"data\":[\"卫生巾\",\"姨妈巾\"]},\n" +
                "  {\"name\":\"个人护理-计生用品\",\"id\":\"100070\",\"data\":[]},\n" +
                "  {\"name\":\"家居清洁-纸类湿巾\",\"id\":\"100071\",\"data\":[]},\n" +
                "  {\"name\":\"家居清洁-清洁用品\",\"id\":\"100072\",\"data\":[]},\n" +
                "  {\"name\":\"家居清洁-居家服饰\",\"id\":\"100073\",\"data\":[]},\n" +
                "  {\"name\":\"五金工具-厨具\",\"id\":\"100074\",\"data\":[]},\n" +
                "  {\"name\":\"五金工具-清洁工具\",\"id\":\"100075\",\"data\":[]},\n" +
                "  {\"name\":\"五金工具-百货/小电器\",\"id\":\"100076\",\"data\":[]},\n" +
                "  {\"name\":\"雪糕冰棍\",\"id\":\"100077\",\"data\":[\"雪糕\",\"冰淇淋\",\"冰激淋\",\"冰糕\"]},\n" +
                "  {\"name\":\"速冻生鲜\",\"id\":\"100078\",\"data\":[]},\n" +
                "  {\"name\":\"其他商品\",\"id\":\"100079\",\"data\":[]},\n" +
                "  {\"name\":\"未分类\",\"id\":\"100080\",\"data\":[]}\n" +
                "]");

        int size1 = objects.size();
        Map<Integer, String> mapping = new HashMap<>(size1);
        for (int i = 0; i < size1; i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            Integer id = jsonObject.getInteger("id");
            String name = jsonObject.getString("name");
            mapping.put(id, name);
        }

        Map<Integer, Map<String, Integer>> map = new HashMap<>();

        CountRequest countRequest = new CountRequest("standard_goods");
        CountResponse count = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        long total = count.getCount();
        int size = 1000;
        long pages = total % size == 0 ? total / size : total / size + 1;
        for (long i = 0; i < pages; i++) {
            SearchRequest searchRequest = new SearchRequest("standard_goods");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.from((int) (i * size)).size(size);
            searchRequest.source(sourceBuilder);

            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                JSONObject jsonObject = JSONObject.parseObject(sourceAsString);
                Integer gcId = jsonObject.getInteger("gcId");
                String token = jsonObject.getString("token");
                Map<String, Integer> innerMap = map.computeIfAbsent(gcId, key -> new HashMap<>(512));
                if (innerMap.containsKey(token)) {
                    innerMap.put(token, innerMap.get(token) + 1);
                } else {
                    innerMap.put(token, 1);
                }
            }
        }

        Set<Map.Entry<Integer, Map<String, Integer>>> entries = map.entrySet();
        int sheetNo = 0;
        WriteWorkbook writeWorkbook = new WriteWorkbook();
        writeWorkbook.setExcelType(ExcelTypeEnum.XLSX);
        writeWorkbook.setFile(new File("/Users/husen/data/token/分词.xlsx"));
        writeWorkbook.setNeedHead(true);
        ExcelWriter writer = new ExcelWriter(writeWorkbook);
        for (Map.Entry<Integer, Map<String, Integer>> entry : entries) {
            Integer key = entry.getKey();
            Map<String, Integer> inner = entry.getValue();
            List<Token> tokens = new ArrayList<>();
            inner.forEach((token, c) -> {
                Token t = new Token();
                t.setToken(token);
                t.setCount(c);
                tokens.add(t);
            });
            WriteSheet writeSheet = new WriteSheet();
            writeSheet.setClazz(Token.class);
            writeSheet.setSheetName(mapping.get(key).replace("/", ""));
            writeSheet.setSheetNo(sheetNo);
            tokens.sort(Comparator.comparingInt(t -> (-t.getCount())));
            writer.write(tokens, writeSheet);
            sheetNo++;
        }
        System.out.println("完成");
        writer.finish();
    }

    private boolean isNumber(String number) {
        int index = number.indexOf(".");
        if (index < 0) {
            return StringUtils.isNumeric(number);
        } else {
            String num1 = number.substring(0, index);
            String num2 = number.substring(index + 1);
            return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);
        }
    }

    public void run() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), r -> new Thread(r, "StandardGoods"));

        ZipSecureFile.setMinInflateRatio(-1.0d);
        EasyExcel.read("/Users/husen/t_goods_common_image.xlsx", StandardGoods.class, new AnalysisEventListener<StandardGoods>() {

            private final Logger LOGGER = LoggerFactory.getLogger("AnalysisEventListener");

            @Override
            public void invoke(StandardGoods data, AnalysisContext context) {
                LOGGER.info("解析到一条数据:{}", data);
                if (data.getGcId() == null) {
                    return;
                }
                executor.execute(() -> {
                    List<String> tokens = doAnalyze(data.getGcId(), data.getGoodsName());
                    tokens.forEach(token -> save(data.getGcId(), token));
                });
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                LOGGER.info("所有数据解析完成！");
            }
        }).sheet().doRead();
    }

    private List<String> doAnalyze(Integer gcId, String content) {
        List<String> result = new ArrayList<>();
        try {
            Request request = new Request("GET", "_analyze");
            JSONObject entity = new JSONObject();
            entity.put("analyzer", "ik_max_word");
            entity.put("text", content);
            request.setJsonEntity(entity.toJSONString());
            Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            JSONArray tokens = jsonObject.getJSONArray("tokens");
            int size = tokens.size();
            for (int i = 0; i < size; i++) {
                String token = JSONObject.parseObject(tokens.getString(i)).getString("token");
                result.add(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分词异常:{}, {}", gcId, content);
        }
        return result;
    }

    private void save(Integer gcId, String token) {
        try {
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
            jsonBuilder.startObject();
            {
                jsonBuilder.field("gcId", gcId.intValue());
                jsonBuilder.field("token", token);
            }
            jsonBuilder.endObject();
            IndexRequest indexRequest = new IndexRequest("standard_goods")
                    .id(UUID.randomUUID().toString()).source(jsonBuilder);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.warn("save token error: {}, {}", gcId, token);
        }
    }
}
