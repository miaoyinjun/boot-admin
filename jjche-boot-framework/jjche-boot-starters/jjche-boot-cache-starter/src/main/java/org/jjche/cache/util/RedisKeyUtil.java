package org.jjche.cache.util;

/**
 * <p>
 * redis 关系型数据设计
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-20
 */
public class RedisKeyUtil {
    /**
     * <p>
     * redis的key
     * </p>
     * <p>
     * 形式为：表名:主键名:主键值:列名
     * </p>
     *
     * @param tableName     表名
     * @param majorKey      主键名
     * @param majorKeyValue 主键值
     * @param column        列名
     * @return key
     * @author miaoyj
     * @since 2020-07-20
     */
    public static String getKeyWithColumn(String tableName, String majorKey, String majorKeyValue, String column) {
        StringBuffer buffer = new StringBuffer(getKey(tableName, majorKey, majorKeyValue));
        buffer.append(column);
        return buffer.toString();
    }

    /**
     * <p>
     * redis的key
     * </p>
     * <p>
     * 形式为：表名:主键名:主键值
     * </p>
     *
     * @param tableName     表名
     * @param majorKey      主键名
     * @param majorKeyValue 主键值
     * @return key
     * @author miaoyj
     * @since 2020-07-20
     */
    public static String getKey(String tableName, String majorKey, String majorKeyValue) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(tableName).append(":");
        buffer.append(majorKey).append(":");
        buffer.append(majorKeyValue).append(":");
        return buffer.toString();
    }
}
