package org.jjche.cloud.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.jjche.common.exception.FeignRException;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * R捕捉feign异常
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-06-10
 */
@Configuration
public class FeignErrorDecoderR implements ErrorDecoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = Util.toString(response.body().asReader(Charset.defaultCharset()));
            return new FeignRException(body);
        } catch (IOException e) {
        }
        return decode(methodKey, response);
    }
}