package org.jjche.cat.constant;

/**
 * <p>
 * CAT常量
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
public interface CatConstant {
    /**
     * 服务器分隔 {@value}
     */
    String SERVER_DELIMITER = ";";
    /**
     * 服务器端口分隔 {@value}
     */
    String PORT_DELIMITER = ":";

    /**
     * 属性前缀 {@value}
     */
    String JJCHE_CAT_PROPERTY_PREFIX = "jjche.cat";

    /**
     * 类型-方法 {@value}
     */
    String TYPE_METHOD = "Method";

    /**
     * 类型-SQL {@value}
     */
    String TYPE_SQL = "SQL";

    /**
     * 类型-SQL-方法 {@value}
     */
    String TYPE_SQL_METHOD = "SQL.Method";

    /**
     * 类型-SQL-数据库 {@value}
     */
    String TYPE_SQL_DB = "SQL.Database";
}
