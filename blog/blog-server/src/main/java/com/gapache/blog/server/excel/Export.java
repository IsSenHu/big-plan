package com.gapache.blog.server.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/8/20 11:29 上午
 */
@Slf4j
public class Export {

    public static void main(String[] args) throws Exception {
        List<Shop> success = new ArrayList<>();

        String[] list = new File("/Users/husen/data/cd").list();
        List<String> errors = new ArrayList<>();
        for (String name : Objects.requireNonNull(list)) {
            if (name.startsWith("error")) {
                errors.add(name);
            }
        }

        for (String error : errors) {
            InputStream in = new FileInputStream("/Users/husen/data/cd/" + error);
            byte[] bytes = FileCopyUtils.copyToByteArray(in);
            String info = new String(bytes, StandardCharsets.UTF_8);

            List<Community> communities = JSONArray.parseArray(info, Community.class);
            if (CollectionUtils.isNotEmpty(communities)) {
                log.info("repeat size:{}", communities.size());

                List<Community> notLocation = new ArrayList<>();
                for (Community community : communities) {
                    if (StringUtils.isBlank(community.getLocation())) {
                        notLocation.add(community);
                    }
                }
                int notLocationSize = notLocation.size();
                log.info("notLocation size:{}", notLocationSize);

                CountDownLatch countDownLatch = new CountDownLatch(1);
                ChongQing.submit(notLocation, countDownLatch, 999);
                countDownLatch.await();
                communities.removeIf(c -> StringUtils.isBlank(c.getLocation()));

                List<Community> errorTemp = new ArrayList<>();
                List<Shop> shopTemp = new ArrayList<>();
                ChongQing.queryShop(errorTemp, communities, shopTemp);
                success.addAll(shopTemp);

                log.info("again error:{}", JSONObject.toJSONString(errorTemp));
            }
         }

        for (String name : Objects.requireNonNull(list)) {
            if (name.startsWith("success")) {
                InputStream in = new FileInputStream("/Users/husen/data/cd/" + name);
                byte[] bytes = FileCopyUtils.copyToByteArray(in);
                String info = new String(bytes, StandardCharsets.UTF_8);
                List<Shop> shops = JSONArray.parseArray(info, Shop.class);
                if (CollectionUtils.isNotEmpty(shops)) {
                    success.addAll(shops);
                }
            }
        }

        List<Shop> result = new ArrayList<>();
        log.info("before check:{}", success.size());
        Map<String, List<Shop>> uidGrouping = success.stream().collect(Collectors.groupingBy(Shop::getUid));
        uidGrouping.forEach((uid, shops) -> {
            shops.sort(Comparator.comparingInt(Shop::getDistance));
            result.add(shops.get(0));
        });
        result.sort(Comparator.comparing(Shop::getCommunity));
        log.info("after check:{}", result.size());

        WriteWorkbook writeWorkbook = new WriteWorkbook();
        writeWorkbook.setExcelType(ExcelTypeEnum.XLSX);
        writeWorkbook.setFile(new File("/Users/husen/data/cd/cd.xlsx"));
        writeWorkbook.setNeedHead(true);
        ExcelWriter writer = new ExcelWriter(writeWorkbook);
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setClazz(Shop.class);
        writeSheet.setSheetName("超市数据");
        writeSheet.setSheetNo(1);

        writer.write(result, writeSheet);
        writer.finish();

        result.removeIf(s -> !StringUtils.equals(s.getTelephone(), "[]"));
        log.info("has tel:{}", result.size());
    }
}
