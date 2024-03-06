package org.jjche.system.modules.system.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 在线用户
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 岗位
     */
    private String dept;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作系统
     */
    private String os;

    /**
     * IP
     */
    private String ip;

    /**
     * 地址
     */
    private String address;

    /**
     * token
     */
    private String key;

    /**
     * 登录时间
     */
    private Date loginTime;


    /**
     * 最后访问时间
     */
    private Date lastAccessTime;

}
