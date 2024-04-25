package org.jjche.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 日志记录
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-28
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class LogRecordDTO implements Serializable {

    /**
     * 是否保存参数
     */
    private Boolean saveParams;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 描述
     */
    private String value;

    /**
     * 租户
     */
    private String tenant;
    /**
     * 应用名
     */
    private String appName;

    /**
     * 业务key
     * 由prefix与bizNo拼接
     */
    @NotBlank(message = "bizKey required")
    @Length(max = 200, message = "bizKey max length is 200")
    private String bizKey;

    /**
     * 业务编号
     */
    @NotBlank(message = "bizNo required")
    @Length(max = 200, message = "bizNo max length is 200")
    private String bizNo;

    /**
     * 操作人
     */
    @NotBlank(message = "operator required")
    @Length(max = 63, message = "operator max length 63")
    private String operator;

    /**
     * 结果
     */
    @NotBlank(message = "result required")
    private String result;

    /**
     * 操作类型
     */
    @NotBlank(message = "type required")
    private LogType type;

    /**
     * 日志分类
     */
    private LogCategoryType category;
    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 模块
     */
    private String module;

    /**
     * 方法
     */
    private String method;

    /**
     * 详情
     */
    private String detail;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 参数
     */
    private String params;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 异常详细
     */
    private byte[] exceptionDetail;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作系统
     */
    private String os;

    /**
     * url
     */
    private String url;

    /**
     * 条件
     */
    private String condition;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 子模块
     */
    private String subModule;
}
