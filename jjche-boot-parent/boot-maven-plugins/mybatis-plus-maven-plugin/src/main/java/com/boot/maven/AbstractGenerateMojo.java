package com.boot.maven;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * <p>
 * 插件基类，用于属性配置
 * 设计成抽象类主要是用于后期可扩展，共享参数配置。
 * </p>
 *
 * @author miaoyj
 * @since 2020-07-15
 * @version 1.1.0
 */
public abstract class AbstractGenerateMojo extends AbstractMojo {

    /**
     * 公用配置
     */
    @Parameter
    protected GlobalConfig globalConfig;

    /**
     * 数据源配置
     */
    @Parameter(required = true)
    protected DataSourceConfig dataSource;

    /**
     * 数据库表配置
     */
    @Parameter
    protected StrategyConfig strategy;

    /**
     * 包 相关配置
     */
    @Parameter
    protected PackageConfig packageInfo;
}
