package org.jjche.core.fileconf;

import cn.hutool.log.StaticLog;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * <p>
 * 文件上传配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-11-02
 */
@Configuration
public class MultipartConfig {

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.home") + "/.jjche/file/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            if (!tmpFile.mkdirs()) {
                StaticLog.warn("create was not successful.");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
