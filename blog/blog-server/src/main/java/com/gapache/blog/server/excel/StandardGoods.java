package com.gapache.blog.server.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/9/8 1:45 下午
 */
@Slf4j
@Data
@Component
public class StandardGoods {

    @ExcelProperty("goods_name")
    private String goodsName;

    @ExcelProperty("gc_id")
    private Integer gcId;
}
