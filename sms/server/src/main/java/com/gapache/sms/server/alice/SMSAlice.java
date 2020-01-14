package com.gapache.sms.server.alice;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/11
 */
@Slf4j
public class SMSAlice {
    private final static String SUCCESS_CODE = "OK";

    private final String regionId;
    private final String sysDomain;
    private final String sysVersion;
    private final IAcsClient iAcsClient;

    public SMSAlice(String regionId, String accessKeyId, String secret, String sysDomain, String sysVersion) {
        this.regionId = regionId;
        this.sysDomain = sysDomain;
        this.sysVersion = sysVersion;
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
        this.iAcsClient = new DefaultAcsClient(profile);
    }

    private CommonRequest getCommonRequest(MethodType method, SysAction sysAction, Map<String, String> queryParameters) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(method);
        request.setSysDomain(sysDomain);
        request.setSysVersion(sysVersion);
        request.setSysAction(sysAction.getValue());
        request.putQueryParameter("RegionId", regionId);
        if (queryParameters != null) {
            queryParameters.forEach(request::putQueryParameter);
        }
        return request;
    }

    public CommonResponse call(MethodType method, SysAction sysAction, Map<String, String> queryParameters) {
        CommonRequest request = getCommonRequest(method, sysAction, queryParameters);
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            if (log.isDebugEnabled()) {
                log.debug("Call [{}] queryParameters [{}] response:[{}]", sysAction.getValue(), queryParameters, response.getData());
            }
            return response;
        } catch (ClientException e) {
            log.error("Call [{}] queryParameters [{}] fail.", sysAction.getValue(), queryParameters, e);
            return null;
        }
    }

    public boolean fail(@NonNull String code) {
        return !StringUtils.equals(code, SUCCESS_CODE);
    }
}
