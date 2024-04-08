
package org.jjche.filter.enc.field;

/**
 * <p>
 * 字段加密类型
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-08-25
 */
public enum EncryptFieldType {
    /**
     * 加密，解密类型
     */
    ENCRYPT("加密"),
    DECRYPT("解密");

    private String value;

    EncryptFieldType(String value) {
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
