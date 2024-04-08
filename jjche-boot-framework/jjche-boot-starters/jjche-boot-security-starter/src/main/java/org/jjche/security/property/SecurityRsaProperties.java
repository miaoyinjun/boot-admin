package org.jjche.security.property;

import lombok.Data;

/**
 * <p>
 * 密码加密传输，前端公钥加密，后端私钥解密
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-18
 */
@Data
public class SecurityRsaProperties {
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;
}
