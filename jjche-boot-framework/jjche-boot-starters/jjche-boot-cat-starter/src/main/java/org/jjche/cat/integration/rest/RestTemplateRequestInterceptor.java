package org.jjche.cat.integration.rest;

import org.jjche.cat.constant.CatConstantsExt;
import org.jjche.cat.constant.CatTransactionManager;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * <p>
 * RestTemplate 拦截器
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-24
 */
public class RestTemplateRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String name = request.getMethod() + ":" + request.getURI().toString();
        return CatTransactionManager.newTransaction(() -> {
            try {
                return execution.execute(request, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, CatConstantsExt.TYPE_CALL_REST_TEMPLATE, name);
    }
}