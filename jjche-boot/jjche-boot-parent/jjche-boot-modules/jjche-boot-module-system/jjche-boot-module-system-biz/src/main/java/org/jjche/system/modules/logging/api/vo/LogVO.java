package org.jjche.system.modules.logging.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>LogVO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-5-22
 */
@Data
public class LogVO implements Serializable {

    private Long id;
    /**
     * 操作用户
     */
    private String username;

    /**
     * 描述
     */
    private String description;

    /**
     * 查询模块
     */
    private String module;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 地址
     */
    private String address;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 请求耗时
     */
    private Long time;

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
     * 详情
     */
    private String detail;

    /**
     * 是否成功
     */
    private Boolean isSuccess;

    /**
     * 租户
     */
    private String tenant;

    /**
     * 业务key
     * 由prefix与bizNo拼接
     */
    private String bizKey;

    /**
     * 业务编号
     */
    private String bizNo;

    /**
     * 结果
     */
    private String result;

    /**
     * 日志分类
     */
    private String category;

    /**
     * 创建时间
     */
    private Timestamp gmtCreate;

    /**
     * 请求id
     */
    private String requestId;
    /**
     * 应用名
     */
    private String appName;
}
