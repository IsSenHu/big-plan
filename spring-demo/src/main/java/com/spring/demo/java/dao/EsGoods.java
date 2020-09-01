package com.spring.demo.java.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author HuSen
 * @since 2020/8/21 6:19 下午
 */
@Data
@Setting(settingPath = "esGoodsSetting.json")
@Document(indexName = "index_goods")
public class EsGoods {
    /**
     * 商品id(SKU)
     */
    @Id
    private Integer goodsId;
    /**
     * 商品公共表id
     */
    private Integer goodsCommonid;
    /**
     * 商品名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart_pinyin", searchAnalyzer = "ik_smart_pinyin")
    private String goodsName;
    /**
     * 名字修改标记：0 默认；1 已标记
     */
    private Byte goodsNameState;
    private String goodsJingle;
    /**
     * 店铺id
     */
    private Integer storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 商品分类id
     */
    private Integer gcId;
    /**
     * 一级分类id
     */
    private Integer gcId1;
    /**
     * 二级分类id
     */
    private Integer gcId2;
    /**
     * 三级分类id
     */
    private Integer gcId3;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 商品促销价格
     */
    private BigDecimal goodsPromotionPrice;
    /**
     * 促销类型 0无促销，1团购，2限时折扣
     */
    private Byte goodsPromotionType;
    /**
     * 市场价
     */
    private BigDecimal goodsMarketprice;
    /**
     * 商家编号
     */
    private String goodsSerial;
    /**
     * 库存报警值
     */
    private Byte goodsStorageAlarm;
    /**
     * 商品点击数量
     */
    private Integer goodsClick;
    /**
     * 销售数量
     */
    private Integer goodsSalenum;
    /**
     * 排序权重暂用
     */
    private Integer goodsCollect;
    /**
     * 商品库存
     */
    private Integer goodsStorage;
    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品规格序列化
     */
    private String goodsSpec;
    /**
     * 商品状态 0上架，1下架
     */
    private Byte goodsState;
    /**
     * 缺货状态：0 默认 1缺货
     */
    private Byte stockState;
    /**
     * 商品审核 1通过，100未通过，0默认
     */
    private Byte goodsVerify;
    /**
     * 商品添加时间
     */
    private Integer goodsAddtime;
    /**
     * 商品编辑时间
     */
    private Integer goodsEdittime;
    /**
     * 一级地区id
     */
    private Integer areaid1;
    /**
     * 二级地区id
     */
    private Integer areaid2;
    /**
     * 颜色规格id
     */
    private Integer colorId;
    /**
     * 运费模板id
     */
    private Integer transportId;
    /**
     * 运费 0为免运费
     */
    private BigDecimal goodsFreight;
    /**
     * 是否开具增值税发票 1是，0否
     */
    private Byte goodsVat;
    /**
     * 商品推荐 1是，0否 默认0
     */
    private Byte goodsCommend;
    /**
     * 店铺分类id 首尾用,隔开
     */
    private String goodsStcids;
    /**
     * 好评星级
     */
    private Byte evaluationGoodStar;
    /**
     * 评价数
     */
    private Integer evaluationCount;
    /**
     * 是否为虚拟商品 1是，0否
     */
    private Byte isVirtual;
    /**
     * 虚拟商品有效期
     */
    private Integer virtualIndate;
    /**
     * 虚拟商品购买上限
     */
    private Byte virtualLimit;
    /**
     * 是否允许过期退款， 1是，0否
     */
    private Byte virtualInvalidRefund;
    /**
     * 是否为F码商品 1是，0否
     */
    private Byte isFcode;
    /**
     * 是否是预约商品 1是，0否
     */
    private Byte isAppoint;
    /**
     * 是否是预售商品 1是，0否
     */
    private Byte isPresell;
    /**
     * 是否拥有赠品
     */
    private Byte haveGift;
    /**
     * 是否为平台自营
     */
    private Byte isOwnShop;
    private BigDecimal commission;
    private String originCompanyName;
    private Integer originFetchId;
    private Integer originFetchType;
    private Integer originId;
    private String originSkuId;
    private String originTag;
    /**
     * 预设销量
     */
    private Integer defaultSaleNum;
    private Date createdDate;
    private Date modifiedDate;
    private String originLink;
}
