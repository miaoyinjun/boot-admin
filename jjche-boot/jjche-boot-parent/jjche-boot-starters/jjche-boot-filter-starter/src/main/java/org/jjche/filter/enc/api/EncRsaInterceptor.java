//package org.jjche.filter.encryption.api.interceptor;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.core.lang.Assert;
//import cn.hutool.core.util.BooleanUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.crypto.SecureUtil;
//import cn.hutool.crypto.asymmetric.RSA;
//import cn.hutool.crypto.asymmetric.Sign;
//import cn.hutool.crypto.asymmetric.SignAlgorithm;
//import cn.hutool.log.StaticLog;
//import org.jjche.filter.enc.api.enums.FilterEncEnum;
//import org.jjche.common.exception.SignException;
//import org.jjche.filter.property.FilterEncApiProperties;
//import org.jjche.filter.util.EncUtil;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.security.PublicKey;
//import java.util.List;
//
///**
// * <p>
// * RSA接口加密过滤器
// * </p>
// * <p>
// * sign:Base64.encode(RSA私钥(ASCII排序URL参数(空参数不参与)+timestamp+nonce))
// * </p>
// *
// * @author miaoyj
// * @version 1.0.0-SNAPSHOT
// * @since 2020-08-07
// */
//public class EncRsaInterceptor implements HandlerInterceptor {
//    private FilterEncApiProperties encryptionProperties;
//
//    /**
//     * <p>Constructor for EncRsaInterceptor.</p>
//     *
//     * @param encryptionProperties a {@link FilterEncApiProperties} object.
//     */
//    public EncRsaInterceptor(FilterEncApiProperties encryptionProperties) {
//        this.encryptionProperties = encryptionProperties;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String queryString = request.getQueryString();
//        String appIdValue = request.getHeader(FilterEncEnum.APP_ID.getKey());
//        String timestampValue = request.getHeader(FilterEncEnum.TIMESTAMP.getKey());
//        String nonceValue = request.getHeader(FilterEncEnum.NONCE.getKey());
//        String signValue = request.getHeader(FilterEncEnum.SIGN.getKey());
//
//        StaticLog.info("rsaFilter.queryString:{},appId:{},timestamp:{},nonce:{},sign:{}", queryString, appIdValue, timestampValue, nonceValue, signValue);
//
//        /** 根据参数排序后拼接为字符串 */
//        String queryOrderedString = EncUtil.queryOrderedString(queryString);
//
//        /** 校验appId有效性 参数*/
//        String appPrivateKey = null;
//        String appPublicKey = null;
//        List<FilterEncApiProperties> applications = encryptionProperties.getApplications();
//        FilterEncApiProperties applicationProperty = EncUtil.getApplicationProperty(appIdValue, applications);
//        if (ObjectUtil.isNotNull(applicationProperty)) {
//            appPrivateKey = applicationProperty.getPrivateKey();
//            appPublicKey = applicationProperty.getPublicKey();
//        }
//        Assert.notNull(appPrivateKey, FilterEncEnum.APP_ID.getErrMsg());
//
//        /** 验证签名 */
//        byte[] data = EncUtil.signData(queryOrderedString, timestampValue, nonceValue);
//
//        RSA rsa = SecureUtil.rsa(appPrivateKey, appPublicKey);
//        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA);
//        PublicKey publicKey = rsa.getPublicKey();
//        byte[] signed = Base64.decode(signValue);
//        sign.setPublicKey(publicKey);
//        boolean verify = sign.verify(data, signed);
//        if (BooleanUtil.isFalse(verify)) {
//            throw new SignException();
//        }
//        return true;
//    }
//}
