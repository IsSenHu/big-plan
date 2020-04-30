package com.gapache.sms.server.alice.request;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/13 13:45
 */
@Slf4j
public abstract class BaseSmsRequest<T extends BaseSmsResponse> implements Serializable {

    private static final long serialVersionUID = -2003637935603373099L;

    private final SMSAlice smsAlice;

    protected BaseSmsRequest(SMSAlice smsAlice) {
        this.smsAlice = smsAlice;
    }

    protected abstract Map<String, String> buildQueryParameters();

    protected abstract SysAction getSysAction();

    protected abstract MethodType getMethodType();

    protected abstract Class<T> responseClazz();

    protected abstract boolean checkQueryParameters();

    public T getResponse() {
        SysAction sysAction = getSysAction();
        Map<String, String> queryParameters = buildQueryParameters();
        if (!checkQueryParameters()) {
            log.warn("QueryParameters is illegal {}", queryParameters);
            return null;
        }
        try {
            CommonResponse response = smsAlice.call(getMethodType(), sysAction, queryParameters);
            if (response == null) {
                return null;
            }
            return JSON.parseObject(response.getData(), responseClazz());
        } catch (Exception e) {
            log.error("[{}] getResponse for [{}].", queryParameters, sysAction, e);
            return null;
        }
    }

    protected Map<String, String> buildAddOrModifyTemplateQueryParameters(String templateType, String templateName, String templateContent, String remark) {
        Map<String, String> queryParameters = new HashMap<>(4);
        queryParameters.put("TemplateType", templateType);
        queryParameters.put("TemplateName", templateName);
        queryParameters.put("TemplateContent", templateContent);
        queryParameters.put("Remark", remark);
        return queryParameters;
    }

    protected Map<String, String> buildAddOrModifySignQueryParameters(String signName, String signSource, String remark, String fileSuffix, String fileContents) {
        Map<String, String> queryParameters = new HashMap<>(5);
        queryParameters.put("SignName", signName);
        queryParameters.put("SignSource", signSource);
        queryParameters.put("Remark", remark);
        if (StringUtils.isNotBlank(fileSuffix)) {
            queryParameters.put("SignFileList.N.FileSuffix", fileSuffix);
        }
        if (StringUtils.isNotBlank(fileContents)) {
            queryParameters.put("SignFileList.N.FileContents", fileContents);
        }
        return queryParameters;
    }
}
