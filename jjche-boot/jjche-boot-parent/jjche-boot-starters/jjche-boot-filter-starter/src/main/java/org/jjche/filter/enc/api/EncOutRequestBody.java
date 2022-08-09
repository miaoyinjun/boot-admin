package org.jjche.filter.enc.api;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.annotation.HttpBodyDecrypt;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.enums.FilterEncEnum;
import org.jjche.common.util.StrUtil;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.core.annotation.controller.OutRestController;
import org.jjche.core.util.RequestHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * <p>
 * 解密body信息
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-04
 */
@RestControllerAdvice(annotations = OutRestController.class)
@RequiredArgsConstructor
public class EncOutRequestBody implements RequestBodyAdvice {
    private final RedisService redisService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //解密注解
        boolean methodHasHttpBodyDecrypt = methodParameter.hasMethodAnnotation(HttpBodyDecrypt.class);
        if (methodHasHttpBodyDecrypt) {
            return true;
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (inputMessage.getBody().available() <= 0) {
            return inputMessage;
        }

        byte[] requestDataByte = IoUtil.readBytes(inputMessage.getBody());
        String requestDataByteNew = null;
        try {
            //解密
            String bodyStr = StrUtil.str(requestDataByte, Charset.defaultCharset());
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            String appIdValue = request.getHeader(FilterEncEnum.APP_ID.getKey());
            SecurityAppKeyBasicVO appSecretVO = redisService.objectGetObject(CacheKey.SECURITY_APP_ID + appIdValue, SecurityAppKeyBasicVO.class);
            String encKey = appSecretVO.getEncKey();
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, encKey.getBytes());

            requestDataByteNew = aes.decryptStr(bodyStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("解密失败");
        }
        // 使用解密后的数据，构造新的读取流
        InputStream rawInputStream = IoUtil.toStream(requestDataByteNew.getBytes());
        return new HttpInputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return rawInputStream;
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}