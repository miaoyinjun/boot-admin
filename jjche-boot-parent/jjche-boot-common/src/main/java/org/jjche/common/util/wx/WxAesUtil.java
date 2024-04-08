package org.jjche.common.util.wx;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * <p>
 * 微信AES工具
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-20
 */
public class WxAesUtil {
    /**
     * Constant <code>initialized=false</code>
     */
    public static boolean initialized = false;

    /**
     * <p>initialize.</p>
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    /**
     * <p>
     * 生成iv
     * </p>
     *
     * @param iv iv
     * @return 结果
     * @throws java.lang.Exception if any.
     */
    public static AlgorithmParameters generateIv(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    /**
     * <p>
     * AES解密
     * </p>
     *
     * @param content content
     * @param keyByte keyByte
     * @param ivByte  ivByte
     * @return 解密数据
     * @throws java.security.InvalidAlgorithmParameterException if any.
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIv(ivByte));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
