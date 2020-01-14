package com.gapache.sms.server.model;

import com.gapache.sms.server.alice.SignSource;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/14 15:59
 */
@Data
public class AddSmsSignVO implements Serializable {
    private static final long serialVersionUID = 947532182521079855L;

    /**
     * 必须
     *
     * 签名名称。
     * 说明 签名必须符合个人用户签名规范或企业用户签名规范。
     */
    private String signName;

    /**
     * 必须
     *
     * 签名来源。其中：
     * 0：企事业单位的全称或简称。
     * 1：工信部备案网站的全称或简称。
     * 2：APP应用的全称或简称。
     * 3：公众号或小程序的全称或简称。
     * 4：电商平台店铺名的全称或简称。
     * 5：商标名的全称或简称
     * 说明 签名来源为1时，请在申请说明中添加网站域名，加快审核速度。
     */
    private SignSource signSource;

    /**
     * 必须
     *
     * 短信签名申请说明。请在申请说明中详细描述您的业务使用场景，申请工信部备案网站的全称或简称请在此处填写域名，长度不超过200个字符。
     */
    private String remark;

    /**
     * 签名的证明文件格式，支持上传多张图片。当前支持jpg、png、gif或jpeg格式的图片。
     * 个别场景下，申请签名需要上传证明文件。详细说明请参考个人用户签名规范和企业用户签名规范。
     */
    private String fileSuffix;

    /**
     * 签名的资质证明文件经base64编码后的字符串。图片不超过2 MB。
     * 个别场景下，申请签名需要上传证明文件。详细说明请参考个人用户签名规范和企业用户签名规范。
     */
    private String fileContents;
}
