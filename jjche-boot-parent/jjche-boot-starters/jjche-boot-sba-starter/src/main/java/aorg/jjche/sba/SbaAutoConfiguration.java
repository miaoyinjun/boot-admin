package aorg.jjche.sba;

import aorg.jjche.sba.conf.SbaSecurityConfig;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * <p>
 * 包以org.开头会导致启动失败，暂未找到解决办法
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({SbaSecurityConfig.class})
@Configuration
@EnableAdminServer
public class SbaAutoConfiguration {
}