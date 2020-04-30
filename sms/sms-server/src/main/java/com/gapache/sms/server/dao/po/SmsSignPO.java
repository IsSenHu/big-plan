package com.gapache.sms.server.dao.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/15 14:47
 */
@Getter
@Setter
@Entity
@Table(name = "tb_sms_sign")
public class SmsSignPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createDate;

    @Column
    private String reason;

    @Column(nullable = false, unique = true)
    private String signName;

    /**
     * 签名审核状态。其中：
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    @Column
    private Integer signStatus;
}
