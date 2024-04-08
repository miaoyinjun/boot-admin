package org.jjche.sys.modules.quartz.enums;

/**
 * <p>
 * 定时器动作枚举
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
public enum QuartzActionEnum {

    ADD("新增"),
    UPDATE("修改"),
    PAUSE("暂停"),
    EXEC("运行"),
    DEL("删除 "),

    ;

    /**
     * 描述
     */
    private String description;

    QuartzActionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
