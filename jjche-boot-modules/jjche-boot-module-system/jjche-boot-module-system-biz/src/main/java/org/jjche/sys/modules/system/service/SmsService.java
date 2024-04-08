package org.jjche.sys.modules.system.service;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.sys.property.AdminProperties;
import org.jjche.sys.property.AliYunSmsCodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 阿里云短信服务
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
@Service
public class SmsService {

    @Autowired
    private AdminProperties adminProperties;

    /**
     * <p>getTemplateCodeRegister.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTemplateCodeRegister() {
        AliYunSmsCodeProperties smsVerificationCodeProperty = adminProperties.getSms();
        return smsVerificationCodeProperty.getTemplateCodeRegister();
    }

    /**
     * <p>getTemplateCodeForgetPwd.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTemplateCodeForgetPwd() {
        AliYunSmsCodeProperties smsVerificationCodeProperty = adminProperties.getSms();
        return smsVerificationCodeProperty.getTemplateCodeForgetPwd();
    }

    /**
     * <p>getTemplateCodeLogin.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTemplateCodeLogin() {
        AliYunSmsCodeProperties smsVerificationCodeProperty = adminProperties.getSms();
        return smsVerificationCodeProperty.getTemplateCodeLogin();
    }

    /**
     * 发送验证码
     *
     * @param phone        a {@link java.lang.String} object.
     * @param templateCode a {@link java.lang.String} object.
     * @param smsCode      a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean sendSmsValidCode(String phone, String templateCode, String smsCode) {
        smsCode = "{\"code\":" + "'" + smsCode + "'" + "}";
        return sendSms(phone, templateCode, smsCode);
    }

    /**
     * 发送短信
     *
     * @param phone        a {@link java.lang.String} object.
     * @param templateCode a {@link java.lang.String} object.
     * @param sms          a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean sendSms(String phone, String templateCode, String sms) {
        boolean isSuccess = true;
        AliYunSmsCodeProperties smsVerificationCodeProperty = adminProperties.getSms();
        try {
            String accessKeyId = smsVerificationCodeProperty.getAccessKeyId();
            String accessSecret = smsVerificationCodeProperty.getAccessSecret();
            String signName = smsVerificationCodeProperty.getSignName();

            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setProtocol(ProtocolType.HTTPS);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", sms);
            try {
                StaticLog.info("sms request: {}", JSONUtil.toJsonStr(request));
                CommonResponse response = client.getCommonResponse(request);
                StaticLog.info("sms result: {}", response.getData());
            } catch (ServerException e) {
                StaticLog.error(ThrowableUtil.getStackTrace(e));
                isSuccess = false;
            } catch (ClientException e) {
                StaticLog.error(ThrowableUtil.getStackTrace(e));
                isSuccess = false;
            }
        } catch (Exception e) {
            StaticLog.error(ThrowableUtil.getStackTrace(e));
            isSuccess = false;
        }
        return isSuccess;
    }

}
