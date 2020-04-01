package com.gapache.sms.server.dao.po;

import com.gapache.sms.server.alice.TemplateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/15 14:16
 */
@Getter
@Setter
@Entity
@Table(name = "tb_sms_template")
public class SmsTemplatePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String templateContent;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @Column(nullable = false)
    private String templateName;

    @Column(nullable = false, unique = true)
    private String templateCode;

    @Column
    private LocalDateTime createDate;

    @Column
    private String reason;

    /**
     * 模板审核状态。其中：
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    @Column
    private Integer templateStatus;
}
