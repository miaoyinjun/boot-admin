
package com.boot.admin.security.property;

import lombok.Data;

/**
 * 登录验证码配置信息
 *
 * @author: liaojinlong
 * @since: 2020/6/10 18:53
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class SecurityLoginCodeProperties {

    /**
     * 验证码配置
     */
    private LoginCodeEnum codeType;
    /**
     * 验证码有效期 分钟
     */
    private Long expiration = 2L;
    /**
     * 验证码内容长度
     */
    private int length = 2;
    /**
     * 验证码宽度
     */
    private int width = 111;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private int fontSize = 25;

    /**
     * <p>Getter for the field <code>codeType</code>.</p>
     *
     * @return a {@link com.boot.admin.security.property.LoginCodeEnum} object.
     */
    public LoginCodeEnum getCodeType() {
        return codeType;
    }
}
