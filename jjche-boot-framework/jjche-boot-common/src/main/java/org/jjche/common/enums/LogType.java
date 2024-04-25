package org.jjche.common.enums;

/**
 * <p>日志类型</p>
 *
 * @author nikohuang
 * @version 1.0.8-SNAPSHOT
 * @author: liaojinlong
 * @since: 2020/6/11 19:47
 */
public enum LogType {
    /**
     * 增删改查
     */
    ADD("新增"),
    SELECT("查询"),
    UPDATE("更新"),
    DELETE("删除");
    private String value;

    LogType(String value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
