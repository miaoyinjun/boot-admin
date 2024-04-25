package org.jjche.cloud.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Configuration
public class FeignConfig {
    @Autowired
    private SecurityProperties properties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String authHeader = securityJwtProperties.getHeader();
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                StaticLog.debug("Feign request: {}", request.getRequestURI());
                // 将token信息放入header中
                String token = request.getHeader(authHeader);
                if (token == null || "".equals(token)) {
                    token = request.getParameter("token");
                }
                StaticLog.debug("Feign request token: {}", token);
                requestTemplate.header(authHeader, token);
                // 传递用户信息请求头，防止丢失
                String userId = request.getHeader(SecurityConstant.JWT_KEY_USER_ID);
                String username = request.getHeader(SecurityConstant.JWT_KEY_USERNAME);
                Enumeration<String> elPermissions = request.getHeaders(SecurityConstant.JWT_KEY_PERMISSION);
                Enumeration<String> dataScopeDeptIds = request.getHeaders(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS);
                String dataScopeIsAll = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL);
                String dataScopeIsSelf = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF);
                String dataScopeUserid = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID);
                String dataScopeUsername = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME);
                String grayVersion = request.getHeader(SecurityConstant.FEIGN_GRAY_TAG);

                requestTemplate.header(SecurityConstant.JWT_KEY_USER_ID, userId);
                requestTemplate.header(SecurityConstant.JWT_KEY_USERNAME, username);
                requestTemplate.header(SecurityConstant.JWT_KEY_PERMISSION, CollUtil.newArrayList(elPermissions));
                //数据范围
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS, CollUtil.newArrayList(dataScopeDeptIds));
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL, dataScopeIsAll);
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF, dataScopeIsSelf);
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID, dataScopeUserid);
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME, dataScopeUsername);
                //灰度
                requestTemplate.header(SecurityConstant.FEIGN_GRAY_TAG, grayVersion);
            }
        };
    }

    /**
     * Feign 客户端的日志记录，默认级别为NONE
     * Logger.Level 的具体级别如下：
     * NONE：不记录任何信息
     * BASIC：仅记录请求方法、URL以及响应状态码和执行时间
     * HEADERS：除了记录 BASIC级别的信息外，还会记录请求和响应的头信息
     * FULL：记录所有请求与响应的明细，包括头信息、请求体、元数据
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Feign支持文件上传
     *
     * @param messageConverters
     * @return
     */
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}