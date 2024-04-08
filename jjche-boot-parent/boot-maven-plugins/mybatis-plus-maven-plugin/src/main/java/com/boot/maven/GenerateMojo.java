package com.boot.maven;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.boot.mybatis.base.entity.BaseEntity;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成文件
 *
 * @author YangHu
 * @version 1.1.0
 * @since 2016/8/30
 */
@Mojo(name = "generate", threadSafe = true)
public class GenerateMojo extends AbstractGenerateMojo {

    /**
     * {@inheritDoc}
     *
     * <p>
     * 入口
     * </p>
     * @author miaoyj
     * @since 2020-07-15
     */
    @Override
    public void execute() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        String baseDir = this.globalConfig.getOutputDir();
        // 全局配置
        this.globalConfig.setOutputDir(baseDir + "/src/main/java/");
        this.globalConfig.setOpen(false);
        mpg.setGlobalConfig(this.globalConfig);

        // 数据源配置
        mpg.setDataSource(this.dataSource);

        // 包配置
        mpg.setPackageInfo(this.packageInfo);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(1);
                this.setMap(map);
            }
        };

        TemplateConfig templateConfig = new TemplateConfig();
        //删除默认的xml模板路径，使用下面的自定义配置
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        //如果模板引擎是freemarker
        String templatePath = ConstVal.TEMPLATE_XML + ".ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return baseDir + "/src/main/resources/mapper/" + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(BaseEntity.class);

        strategy.setSuperServiceClass("com.boot.mybatis.base.service.IMyService");
        strategy.setSuperServiceImplClass("com.boot.mybatis.base.service.MyServiceImpl");

        strategy.setSuperMapperClass("com.boot.mybatis.base.MyBaseMapper");

        strategy.setExclude("DATABASECHANGELOGLOCK", "DATABASECHANGELOG");
        strategy.setSuperEntityColumns("id", "is_deleted", "gmt_create", "gmt_modified", "created_by", "updated_by");

        strategy.setSuperControllerClass("org.jjche.core.base.BaseController");

        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
