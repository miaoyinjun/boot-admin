package org.jjche.cloud;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 入口
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-06-18 9:16
 */
@SpringBootApplication
@EnableAdminServer
public class JjcheCloudMonitorApplication {
    /**
     * 入口
     *
     * @param args an array of {@link java.lang.String} objects.
     * @author miaoyj
     * @since 2020-07-09
     */
    public static void main(String[] args) {
        SpringApplication.run(JjcheCloudMonitorApplication.class, args);
    }
}
