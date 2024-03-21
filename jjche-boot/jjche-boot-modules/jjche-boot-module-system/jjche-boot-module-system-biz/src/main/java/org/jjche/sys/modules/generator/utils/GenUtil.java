package org.jjche.sys.modules.generator.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.extra.template.*;
import org.jjche.common.util.StrUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.sys.modules.generator.domain.ColumnInfoDO;
import org.jjche.sys.modules.generator.domain.GenConfigDO;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;

import static org.jjche.core.util.FileUtil.SYS_TEM_DIR;

/**
 * 代码生成
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-02
 */
public class GenUtil {

    /**
     * Constant <code>PK="PRI"</code>
     */
    public static final String PK = "PRI";
    /**
     * Constant <code>EXTRA="auto_increment"</code>
     */
    public static final String EXTRA = "auto_increment";
    /**
     * Constant <code>API_VERSION_JAVA="ApiVersion.java"</code>
     */
    public static final String API_VERSION_JAVA = "ApiVersion.java";
    /**
     * 默认字段
     */
    public static final Set<String> DEFAULT_COLUMNS = CollUtil.newHashSet("created_by",
            "updated_by", "gmt_create", "gmt_modified");
    private static final String TIMESTAMP = "Timestamp";
    private static final String BIG_DECIMAL = "BigDecimal";

    private static final String DIFF_OLD_FUNC = "DiffOldByIdFunc";

    /**
     * 获取后端代码模板名称
     *
     * @return List
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");
        templateNames.add("DTO");
        templateNames.add("VO");
        templateNames.add("Mapper");
        templateNames.add("MapperXml");
        templateNames.add("Controller");
        templateNames.add("QueryCriteria");
        templateNames.add("ServiceImpl");
        templateNames.add("MapStruct");
//        templateNames.add("SortEnum");
        templateNames.add("ApiVersion");
        templateNames.add("MenuRoleSql");
        templateNames.add(DIFF_OLD_FUNC);
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     *
     * @return List
     */
    private static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("index");
        templateNames.add("api");
        return templateNames;
    }

    public static List<Map<String, Object>> preview(List<ColumnInfoDO> columns, GenConfigDO genConfig, String apiPrefix) {
        Map<String, Object> genMap = getGenMap(columns, genConfig, apiPrefix);
        List<Map<String, Object>> genList = new ArrayList<>();
        // 获取后端模版
        List<String> templates = getAdminTemplateNames();
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        // 获取前端模版
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            map.put(templateName, template.render(genMap));
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        return genList;
    }

    /**
     * <p>download.</p>
     *
     * @param columns   a {@link List} object.
     * @param genConfig a {@link GenConfigDO} object.
     * @return a {@link String} object.
     * @throws IOException if any.
     */
    public static String download(List<ColumnInfoDO> columns, GenConfigDO genConfig, String apiPrefix) throws IOException {
        // 拼接的路径：/tmp/jjche-boot-eladmin-gen-temp/，这个路径在Linux下需要root用户才有权限创建,非root用户会权限错误而失败，更改为： /tmp/jjche-boot-eladmin-gen-temp/
        // String tempPath =SYS_TEM_DIR + "jjche-boot-eladmin-gen-temp" + File.separator + genConfig.getTableName() + File.separator;
        String tempPath = SYS_TEM_DIR + "jjche-boot-eladmin-gen-temp" + File.separator + genConfig.getTableName() + File.separator;
        Map<String, Object> genMap = getGenMap(columns, genConfig, apiPrefix);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, genConfig, genMap.get("className").toString(), tempPath);
            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            String filePath = getFrontFilePath(templateName, genConfig.getModuleName(), genMap.get("tableName").toString(), tempPath);
            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        String packagePath = getPackPath(genConfig.getModuleName(), tempPath);
        return packagePath;
    }

    /**
     * <p>generatorCode.</p>
     *
     * @param columnInfos a {@link List} object.
     * @param genConfig   a {@link GenConfigDO} object.
     * @throws IOException if any.
     */
    public static void generatorCode(List<ColumnInfoDO> columnInfos, GenConfigDO genConfig, String apiPrefix) throws IOException {
        Map<String, Object> genMap = getGenMap(columnInfos, genConfig, apiPrefix);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
//        String userDir = System.getProperty("user.dir");
        String userDir = StrUtil.replace(getApplicationClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "/target/classes/", "");
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, genConfig, genMap.get("className").toString(), userDir);

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)
                    && !file.getName().equalsIgnoreCase(API_VERSION_JAVA)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }

        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            String filePath = getFrontFilePath(templateName, genConfig.getModuleName(), genMap.get("tableName").toString(), userDir);

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
    }

    // 获取模版数据
    private static Map<String, Object> getGenMap(List<ColumnInfoDO> columnInfos, GenConfigDO genConfig, String apiPrefix) {
        String tableName = genConfig.getTableName();
        // 存储模版字段数据
        Map<String, Object> genMap = new HashMap<>(16);
        String apiVersion = genConfig.getApiVersion();
        apiVersion = StrUtil.subBefore(apiVersion, "-", true);
        apiVersion = StrUtil.replace(apiVersion, ".", "_");
        genMap.put("apiVersionConstant", "VERSION_" + apiVersion);
        apiVersion += "版本-" + DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN);
        // 接口别名
        genMap.put("apiAlias", genConfig.getApiAlias());
        apiVersion = StrUtil.replace(apiVersion, "_", ".");
        genMap.put("apiVersion", apiVersion);
        // 包名称
        String packagePath = getSpringBootApplicationPackagePath().
                replaceAll("\\\\|/", ".");

        String packageService = packagePath + ".modules." + genConfig.getModuleName();
        String packageApi = packageService + ".api";

        genMap.put("packagePath", packagePath);
        genMap.put("packageApi", packageApi);
        genMap.put("packageService", packageService);
        // 作者
        genMap.put("author", genConfig.getAuthor());
        // 创建日期
        genMap.put("date", LocalDate.now().toString());
        // 表名
        genMap.put("tableName", tableName);
        // 大写开头的类名
        String className = StrUtil.toCapitalizeCamelCase(tableName);
        // 小写开头的类名
        String changeClassName = StrUtil.toCamelCase(tableName);
        //设置修改前的数据到变量方法
        String diffOldFuncName = StrUtil.swapCase(tableName);
        diffOldFuncName += "_DIFF_OLD_BY_ID";
        // 判断是否去除表前缀
        if (StrUtil.isNotEmpty(genConfig.getPrefix())) {
            className = StrUtil.toCapitalizeCamelCase(StrUtil.removePrefix(tableName, genConfig.getPrefix()));
            changeClassName = StrUtil.toCamelCase(StrUtil.removePrefix(tableName, genConfig.getPrefix()));
        }
        String controllerBaseUrl = StrUtil.pluralizeWord(tableName);
        // 保存类名
        genMap.put("className", className);
        // 保存小写开头的类名
        genMap.put("changeClassName", changeClassName);
        //全大写类名
        genMap.put("diffOldFuncName", diffOldFuncName);
        //控制器基础url
        genMap.put("controllerBaseUrl", controllerBaseUrl);
        // 存在 Timestamp 字段
        genMap.put("hasTimestamp", false);
        // 查询类中存在 Timestamp 字段
        genMap.put("queryHasTimestamp", false);
        // 存在 BigDecimal 字段
        genMap.put("hasBigDecimal", false);
        // 查询类中存在 BigDecimal 字段
        genMap.put("queryHasBigDecimal", false);
        // 是否需要创建查询
        genMap.put("hasQuery", false);
        // 自增主键
        genMap.put("auto", false);
        // 存在字典
        genMap.put("hasDict", false);
        // 存在日期注解
        genMap.put("hasDateAnnotation", false);
        // 保存字段信息
        List<Map<String, Object>> columns = new ArrayList<>();
        // 保存查询字段的信息
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        // 存储字典信息
        List<String> dicts = new ArrayList<>();
        // 存储 between 信息
        List<Map<String, Object>> betweens = new ArrayList<>();
        // 存储不为空的字段信息
        List<Map<String, Object>> isNotNullColumns = new ArrayList<>();

        String superEntityClassName = "BaseEntity";
        for (ColumnInfoDO column : columnInfos) {
            /**
             * 是否包含逻辑删除字段
             */
            if (column.getColumnName().equalsIgnoreCase("is_deleted")) {
                superEntityClassName = "BaseEntityLogicDelete";
            }
            Map<String, Object> listMap = new HashMap<>(16);
            String columnName = column.getColumnName();
            // 字段描述
            listMap.put("remark", column.getRemark());
            // 字段类型
            listMap.put("columnKey", column.getKeyType());
            // 主键类型
            String colType = ColUtil.cloToJava(column.getColumnType());
            // 小写开头的字段名
            String changeColumnName = StrUtil.toCamelCase(columnName);
            // 大写开头的字段名
            String capitalColumnName = StrUtil.toCapitalizeCamelCase(column.getColumnName());
            if (PK.equals(column.getKeyType())) {
                // 存储主键类型
                genMap.put("pkColumnType", colType);
                // 存储小写开头的字段名
                genMap.put("pkChangeColName", changeColumnName);
                // 存储大写开头的字段名
                genMap.put("pkCapitalColName", capitalColumnName);
            }
            // 是否存在 Timestamp 类型的字段
            if (TIMESTAMP.equals(colType)) {
                genMap.put("hasTimestamp", true);
            }
            // 是否存在 BigDecimal 类型的字段
            if (BIG_DECIMAL.equals(colType)) {
                genMap.put("hasBigDecimal", true);
            }
            // 主键是否自增
            if (EXTRA.equals(column.getExtra())) {
                genMap.put("auto", true);
            }
            // 主键存在字典
            if (StrUtil.isNotBlank(column.getDictName())) {
                genMap.put("hasDict", true);
                dicts.add(column.getDictName());
            }

            // 存储字段类型
            listMap.put("columnType", colType);
            // 存储字原始段名称
            listMap.put("columnName", column.getColumnName());
            // 不为空
            listMap.put("istNotNull", column.getNotNull());
            // 字段列表显示
            listMap.put("columnShow", column.getListShow());
            // 表单显示
            listMap.put("formShow", column.getFormShow());
            // 表单组件类型
            listMap.put("formType", StrUtil.isNotBlank(column.getFormType()) ? column.getFormType() : "Input");
            // 小写开头的字段名称
            listMap.put("changeColumnName", changeColumnName);
            //大写开头的字段名称
            listMap.put("capitalColumnName", capitalColumnName);
            // 字典名称
            listMap.put("dictName", column.getDictName());
            //最大长度
            listMap.put("maxLength", column.getMaxLength());
            // 日期注解
            listMap.put("dateAnnotation", column.getDateAnnotation());
            if (StrUtil.isNotBlank(column.getDateAnnotation())) {
                genMap.put("hasDateAnnotation", true);
            }
            // 添加非空字段信息
            if (column.getNotNull()) {
                isNotNullColumns.add(listMap);
            }
            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!StrUtil.isBlank(column.getQueryType())) {
                // 查询类型
                listMap.put("queryType", column.getQueryType());
                // 是否存在查询
                genMap.put("hasQuery", true);
                if (TIMESTAMP.equals(colType)) {
                    // 查询中存储 Timestamp 类型
                    genMap.put("queryHasTimestamp", true);
                }
                if (BIG_DECIMAL.equals(colType)) {
                    // 查询中存储 BigDecimal 类型
                    genMap.put("queryHasBigDecimal", true);
                }
                if ("between".equalsIgnoreCase(column.getQueryType())) {
                    betweens.add(listMap);
                } else {
                    // 添加到查询列表中
                    queryColumns.add(listMap);
                }
            }
            // 添加到字段列表中
            columns.add(listMap);
        }
        //superEntityClass
        genMap.put("superEntityClass", superEntityClassName);
        // 保存字段列表
        genMap.put("columns", columns);
        // 保存查询列表
        genMap.put("queryColumns", queryColumns);
        // 保存字段列表
        genMap.put("dicts", dicts);
        // 保存查询列表
        genMap.put("betweens", betweens);
        // 保存非空字段信息
        genMap.put("isNotNullColumns", isNotNullColumns);
        return genMap;
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfigDO genConfig, String className, String rootPath) {
        String packagePath = getPackPath(genConfig.getModuleName(), rootPath);
        String packageApiPath = packagePath;
        packageApiPath = packageApiPath + "api/";

        String packageApiConstantPath = StrUtil.subBefore(packagePath, "modules", true);
        if ("Entity".equals(templateName)) {
            return packagePath + "domain" + File.separator + className + "DO.java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "rest" + File.separator + className + "Controller.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if ("MapStruct".equals(templateName)) {
            return packagePath + "mapstruct" + File.separator + className + "MapStruct.java";
        }

        if ("DTO".equals(templateName)) {
            return packageApiPath + "dto" + File.separator + className + "DTO.java";
        }

        if ("VO".equals(templateName)) {
            return packageApiPath + "vo" + File.separator + className + "VO.java";
        }

        if ("ApiVersion".equals(templateName)) {
            return packageApiConstantPath + "constant" + File.separator + API_VERSION_JAVA;
        }

        if ("QueryCriteria".equals(templateName)) {
            return packageApiPath + "dto" + File.separator + className + "QueryCriteriaDTO.java";
        }

        if ("SortEnum".equals(templateName)) {
            return packageApiPath + "enums" + File.separator + className + "SortEnum.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if ("MapperXml".equals(templateName)) {
            return packagePath + "mapper" + File.separator + className + "Mapper.xml";
        }
        if ("MenuRoleSql".equals(templateName)) {
            return packagePath + "sql" + File.separator + className + "MenuRoleSql.sql";
        }
        if (DIFF_OLD_FUNC.equals(templateName)) {
            return packagePath + "function" + File.separator + className + "DiffOldByIdParseFunction.java";
        }
        return null;
    }

    /**
     * 定义前端文件路径以及名称
     */
    private static String getFrontFilePath(String templateName, String path,
                                           String apiName, String rootPath) {
        path = getPackPath(path, rootPath);
        path += File.separator + "ui" + File.separator;
        if ("api".equals(templateName)) {
            return path + "api/" + apiName + "/api.js";
        }

        if ("index".equals(templateName)) {
            return path + "views/" + apiName + "/index.vue";
        }

        return null;
    }

    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        //ApiVersion.java不覆盖，追加
        boolean isApiVersion = FileUtil.exist(file) && file.getName().equalsIgnoreCase(API_VERSION_JAVA);
        // 生成目标文件
        Writer writer = null;
        boolean isApiVertion = false;
        if (isApiVersion) {
            //查看ApiVersion是否包含已经存在的版本
            List<String> strList = FileUtil.readLines(file, Charset.defaultCharset());
            if (CollUtil.isNotEmpty(strList)) {
                String apiVersionConstant = map.get("apiVersionConstant").toString();
                for (String s : strList) {
                    if (-1 != StrUtil.indexOfIgnoreCase(s, apiVersionConstant)) {
                        isApiVertion = true;
                        break;
                    }
                }
                if (!isApiVertion) {
                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
                    template = engine.getTemplate("generator/admin/ApiVersionConstant.ftl");

                    byte[] fileContentByte = FileUtil.readBytes(file);
                    String fileContentStr = new String(fileContentByte);
                    fileContentStr = StrUtil.replace(fileContentStr, fileContentStr.length() - 1, "}", "", true);
                    FileUtil.writeBytes(fileContentStr.getBytes(), file);
                }
            }
        }
        if (!isApiVertion) {
            try {
                FileUtil.touch(file);
                writer = new FileWriter(file, isApiVersion);
                template.render(map, writer);
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                assert writer != null;
                writer.close();
            }
        }
        //API_VERSION_JAVA不覆盖，追加
//        boolean isApiVersionName = file.getName().equalsIgnoreCase("API_VERSION_JAVA");
//        if (isApiVersionName && !isApiVersion) {
//            genFile(file, template, map);
//        }
    }

    /**
     * <p>
     * 获取Spring入口类所在包
     * </p>
     *
     * @return a {@link String} object.
     * @author miaoyj
     * @since 2020-10-10
     */
    public static String getSpringBootApplicationPackagePath() {
        Package pack = getApplicationClass().getPackage();
        return pack.getName().replace(".", File.separator);
    }

    /**
     * <p>
     * 获取Application的class所在位置
     * </p>
     *
     * @return /
     */
    private static Class getApplicationClass() {
        Map<String, Object> map = SpringUtil.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        Assert.isTrue(MapUtil.isNotEmpty(map), "找不到SpringBootApplication注解");
        Object application = null;
        for (String key : map.keySet()) {
            application = map.get(key);
            break;
        }
        return application.getClass();
    }

    /**
     * <p>
     * 获取包位置
     * </p>
     *
     * @param moduleName a {@link String} object.
     * @param rootPath   a {@link String} object.
     * @return 包位置
     * @author miaoyj
     * @since 2020-10-10
     */
    public static String getPackPath(String moduleName, String rootPath) {
        String applicationPackPath = getSpringBootApplicationPackagePath();
        String projectPath = rootPath + File.separator;
        String packagePath = projectPath
                + "src" + File.separator + "main" + File.separator + "java"
                + File.separator + applicationPackPath + File.separator
                + "modules" + File.separator + moduleName + File.separator;
        return packagePath;
    }
}
