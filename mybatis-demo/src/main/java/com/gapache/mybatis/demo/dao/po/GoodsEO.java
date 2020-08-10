package com.gapache.mybatis.demo.dao.po;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;

/**
 * @author HuSen
 * @since 2020/8/7 12:20 上午
 */
@Data
@Document(indexName = "mall4")
public class GoodsEO {

    @Id
    private String goodsBarcode;
    @Field
    private String goodsName;
    @Field
    private Float goodsPrice;
    @Field
    private Long id;
    @Field
    private String query;
    @Field
    private String sql;
}
