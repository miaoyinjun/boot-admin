package org.jjche.common.enums;

/**
 * <p>
 * 日志分类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-26
 */
public enum LogCategoryType {
    /**
     * 增删改查
     */
    MANAGER("管理员"),
    OPERATING("运营"),
    OTHER("其它");
    private String value;

    LogCategoryType(String value) {
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
