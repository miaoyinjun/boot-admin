package org.jjche.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p>WebSocketConfig class.</p>
 *
 * @author ZhangHouYing
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24 15:44
 */
@Configuration
public class WebSocketConfig {

    /**
     * <p>serverEndpointExporter.</p>
     *
     * @return a {@link org.springframework.web.socket.server.standard.ServerEndpointExporter} object.
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
