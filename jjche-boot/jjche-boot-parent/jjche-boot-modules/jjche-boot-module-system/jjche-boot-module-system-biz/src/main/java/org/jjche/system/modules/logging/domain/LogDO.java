package org.jjche.system.modules.logging.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>BizLog class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
@Data
@NoArgsConstructor
@TableName(value = "sys_log")
public class LogDO implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @OrderBy
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
