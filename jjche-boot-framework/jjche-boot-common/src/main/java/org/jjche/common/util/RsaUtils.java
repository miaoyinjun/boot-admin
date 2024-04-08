package org.jjche.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * <p>
 * Rsa 工具类，公钥私钥生成，加解密
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-24
 */
public class RsaUtils {

    /**
     * <p>
     * 私钥解密
     * </p>
     *
     * @param privateKeyText 私钥
     * @param text           待解密的文本
     * @return 解密的文本
     * @author miaoyj
     * @since 2020-09-24
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) {
        RSA rsa = SecureUtil.rsa(privateKeyText, null);
        byte[] decrypt = rsa.decrypt(text, KeyType.PrivateKey);
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param publicKeyText 公钥
     * @param text          待加密的文本
     * @return 加密的文本
     * @author miaoyj
     * @since 2020-09-24
     */
    public static String encryptBypPublicKey(String publicKeyText, String text) {
        RSA rsa = SecureUtil.rsa(null, publicKeyText);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(text, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return Base64.encode(encrypt);
    }
}
