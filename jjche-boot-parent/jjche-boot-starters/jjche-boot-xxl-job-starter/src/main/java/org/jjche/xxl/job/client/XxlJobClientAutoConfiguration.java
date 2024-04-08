package org.jjche.xxl.job.client;

import cn.hutool.log.StaticLog;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.jjche.xxl.job.client.config.XxlJobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Configuration
@EnableConfigurationProperties(value = XxlJobProperties.class)
@ConditionalOnProperty(value = "jjche.xxljob.enabled", havingValue = "true", matchIfMissing = true)
public class XxlJobClientAutoConfiguration {
    @Autowired
    private XxlJobProperties xxlJobProperties;

    /**
     * <p>xxlJobExecutor.</p>
     *
     * @return a {@link com.xxl.job.core.executor.impl.XxlJobSpringExecutor} object.
     */
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        StaticLog.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
//        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
//        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());

        return xxlJobSpringExecutor;
    }
}
