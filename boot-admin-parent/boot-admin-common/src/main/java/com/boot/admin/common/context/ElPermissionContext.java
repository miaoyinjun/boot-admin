package com.boot.admin.common.context;


/**
 * <p>
 * 权限标识临时变量
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-10
 * @version 1.0.1-SNAPSHOT
 */
public class ElPermissionContext {

    private static final InheritableThreadLocal<String> VARIABLE = new InheritableThreadLocal<>();


    /**
     * <p>putVariable.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public static void set(String value) {
        VARIABLE.set(value);
    }

    /**
     * <p>getVariables.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String get() {
        String value = VARIABLE.get();
        clear();
        return value;
    }

    /**
     * <p>clear.</p>
     */
    public static void clear() {
        VARIABLE.remove();
    }
}
