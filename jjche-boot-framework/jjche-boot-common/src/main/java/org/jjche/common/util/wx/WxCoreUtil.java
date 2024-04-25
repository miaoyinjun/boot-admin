package org.jjche.common.util.wx;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.binary.Base64;


/**
 * <p>
 * 微信解密工具
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-20
 */
public class WxCoreUtil {

    private static final String WATERMARK = "watermark";
    private static final String APPID = "appid";

    /**
     * <p>
     * 解密数据
     * </p>
     *
     * @param appId         appId
     * @param encryptedData encryptedData
     * @param sessionKey    sessionKey
     * @param iv            iv
     * @return 结果
     */
    public static String decrypt(String appId, String encryptedData, String sessionKey, String iv) {
        String result = "";
        try {
            WxAesUtil aes = new WxAesUtil();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                result = new String(WxPkcs7Encoder.decode(resultByte));
                JSONObject jsonObject = JSONUtil.parseObj(result);
                String decryptAppid = jsonObject.getJSONObject(WATERMARK).getStr(APPID);
                if (!appId.equals(decryptAppid)) {
                    result = "";
                }
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }


    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {
        String appId = "wxf523a58d3d376221";
        String encryptedData = "C7C5fbjuhNWe3aYnHSjzSoTUWNIXzE1n42xRNrAiO83nWJW5EuXtogWkybsV+xknFQbygtdkY5lXJBmArGTh61Bx9K0fF0nRYqlD3IcSkcEAZ7FWB1DYJjkm0/6O9IZLgMD1LIWqASZXBUqD0TTe0MZIVXmhbnXAFOeqWZL1+4qIFgE/OLqbTE3nYSXR+iFCLTdUai8EbQjwtR41DbtGnA==";

        String iv = "i1Hj2/7QukzTPS2x+Q3X9w==";
//
        String sessionKey = "PMGiF0NsGoB9zanZQiHUXw==";
//
        System.out.println(decrypt(appId, encryptedData, sessionKey, iv));
    }

}
