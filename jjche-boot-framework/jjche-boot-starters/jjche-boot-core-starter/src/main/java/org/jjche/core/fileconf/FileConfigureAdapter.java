package org.jjche.core.fileconf;

import org.jjche.core.constant.FileConstant;
import org.jjche.core.property.CoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * WebMvcConfigurer
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-30
 */
@Configuration
@EnableWebMvc
public class FileConfigureAdapter implements WebMvcConfigurer {

    /**
     * 文件配置
     */
    @Autowired
    private FileProperties properties;

    @Resource
    private CoreProperties coreProperties;

    /**
     * <p>Constructor for FileConfigureAdapter.</p>
     *
     * @param properties a {@link FileProperties} object.
     */
    public FileConfigureAdapter(FileProperties properties) {
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileProperties.ElPath path = properties.getPath();
        String avatarUtl = "file:" + path.getAvatar().replace("\\", "/");
        String pathUtl = "file:" + path.getPath().replace("\\", "/");
        String avatarPathMatch = FileConstant.AVATAR_PATH_MATCH;
        String filePathMatch = FileConstant.FILE_PATH_MATCH;
        registry.addResourceHandler(avatarPathMatch).addResourceLocations(avatarUtl).setCachePeriod(0);
        registry.addResourceHandler(filePathMatch).addResourceLocations(pathUtl).setCachePeriod(0);
    }
}
