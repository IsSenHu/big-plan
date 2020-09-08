package com.gapache.blog.server.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author HuSen
 * @since 2020/9/8 5:16 下午
 */
@Data
public class Token implements Comparable<Token> {
    @ExcelProperty("分词")
    private String token;
    @ExcelProperty("数量")
    private Integer count;

    @Override
    public int compareTo(Token o) {
        return this.count - o.count;
    }
}
