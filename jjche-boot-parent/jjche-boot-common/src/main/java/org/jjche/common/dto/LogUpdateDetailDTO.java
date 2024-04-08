package org.jjche.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 日志修改内容详情
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-16
 */
@Data
public class LogUpdateDetailDTO implements Serializable {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 旧值
     */
    private String oldVal;
    /**
     * 新值
     */
    private String newVal;
}
