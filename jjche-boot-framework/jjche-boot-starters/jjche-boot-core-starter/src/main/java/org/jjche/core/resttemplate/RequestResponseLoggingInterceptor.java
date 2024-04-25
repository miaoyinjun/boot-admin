package org.jjche.core.resttemplate;

import cn.hutool.log.StaticLog;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * RestTemplate 日志
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-24
 */
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        StaticLog.debug("===========================request begin================================================");
        StaticLog.debug("URI         : {}", request.getURI());
        StaticLog.debug("Method      : {}", request.getMethod());
        StaticLog.debug("Headers     : {}", request.getHeaders());
        StaticLog.debug("Request body: {}", new String(body, "UTF-8"));
        StaticLog.debug("==========================request end================================================");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        StaticLog.debug("============================response begin==========================================");
        StaticLog.debug("Status code  : {}", response.getStatusCode());
        StaticLog.debug("Status text  : {}", response.getStatusText());
        StaticLog.debug("Headers      : {}", response.getHeaders());
        StaticLog.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        StaticLog.debug("=======================response end=================================================");
    }
}