package org.jjche.security.property;

import lombok.Data;

/**
 * <p>
 * 令牌配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Data
public class SecurityJwtProperties {
    /**
     * 密钥, 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;
    /**
     * 令牌过期时间 此处单位/毫秒，默认4小时
     */
    private Long tokenValidityInMilliSeconds;
    /**
     * 认证header
     */
    private String header;
    /**
     * 认证前缀
     */
    private String tokenStartWith;

    /**
     * token 续期检查，默认30分钟
     */
    private Long detect;
    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    private String onlineKey;
    /**
     * 验证码 key
     */
    private String codeKey;

    /**
     * <p>Getter for the field <code>tokenStartWith</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
