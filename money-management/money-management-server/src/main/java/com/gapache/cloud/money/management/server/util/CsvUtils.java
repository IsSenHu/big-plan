package com.gapache.cloud.money.management.server.util;

import com.csvreader.CsvReader;
import com.gapache.cloud.money.management.common.model.TransactionDTO;
import com.gapache.commons.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/13 3:45 下午
 */
public class CsvUtils {

    public static List<TransactionDTO> read(InputStream inputStream) {
        CsvReader reader = new CsvReader(inputStream, Charset.forName("gbk"));
        List<TransactionDTO> entities = new ArrayList<>();
        try {
            // 读取表头
            reader.readHeaders();
            // 逐条读取，直至读完
            reader.readRecord();
            reader.readRecord();
            reader.readRecord();
            reader.readRecord();
            while (reader.readRecord()) {
                String transactionId = reader.get(0);
                if (transactionId.startsWith("------------")) {
                    break;
                }
                TransactionDTO dto = new TransactionDTO();
                dto.setTransactionId(transactionId);
                dto.setOrderBn(reader.get(1));
                String createTime = reader.get(2);
                if (StringUtils.isNotBlank(createTime)) {
                    dto.setCreateTime(TimeUtils.parse(TimeUtils.Format._2, createTime));
                }
                String payTime = reader.get(3);
                if (StringUtils.isNotBlank(payTime)) {
                    dto.setPayTime(TimeUtils.parse(TimeUtils.Format._2, payTime));
                }
                String lastModifyTime = reader.get(4);
                if (StringUtils.isNotBlank(lastModifyTime)) {
                    dto.setLastModifyTime(TimeUtils.parse(TimeUtils.Format._2, lastModifyTime));
                }
                dto.setSource(reader.get(5));
                dto.setType(reader.get(6));
                dto.setTarget(reader.get(7));
                dto.setGoodsName(reader.get(8));
                dto.setAmount(BigDecimal.valueOf(Double.parseDouble(reader.get(9))));
                dto.setIncomeOrExpenditure(reader.get(10));
                dto.setStatus(reader.get(11));
                dto.setServiceCost(BigDecimal.valueOf(Double.parseDouble(reader.get(12))));
                dto.setRefund(BigDecimal.valueOf(Double.parseDouble(reader.get(13))));
                dto.setMark(reader.get(14));
                dto.setFundsStatus(reader.get(15));
                entities.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<TransactionDTO> read = read(new FileInputStream("/Users/husen/Downloads/alipay_record_20200811_1002_1.csv"));
        System.out.println(read);
    }
}
