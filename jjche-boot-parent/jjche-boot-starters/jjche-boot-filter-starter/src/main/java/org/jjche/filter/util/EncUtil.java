package org.jjche.filter.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 加密工具
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-14
 */
public class EncUtil {
    /**
     * <p>
     * 过滤空或null的参数，升序排序，拼接url
     * </p>
     *
     * @param queryString url参数
     * @return 处理后的url参数
     * @author miaoyj
     * @since 2020-08-14
     */
    public static String queryOrderedString(String queryString) {
        /** 参数转换map */
        UrlQuery urlQuery = UrlQuery.of(queryString, StandardCharsets.UTF_8);
        Map<CharSequence, CharSequence> urlQueryMap = urlQuery.getQueryMap();
        /** 过滤空或null 参数*/
        urlQueryMap = urlQueryMap.entrySet().stream().filter(x -> {
            return StrUtil.isNotBlank(x.getValue());
        }).collect(Collectors.toMap(h -> h.getKey(), h -> h.getValue()));
        /** 根据参数排序后拼接为字符串 */
        return MapUtil.sortJoin(urlQueryMap, "&", "=", true);
    }

    /**
     * <p>
     * md5签名
     * </p>
     *
     * @param appKey             应用标记
     * @param timestampValue     时间戳
     * @param nonceValue         随机数
     * @return md5签名
     * @author miaoyj
     * @since 2020-08-14
     */
    public static String md5Sign(String appKey, String timestampValue, String nonceValue) {
        /** 拼接待签名字符串 */
        StringBuilder signStrBuild = new StringBuilder();
        signStrBuild.append(appKey);
        signStrBuild.append(timestampValue);
        signStrBuild.append(nonceValue);
        /** md5生成16进制小写字符串 */
        return SecureUtil.md5().digestHex16(signStrBuild.toString());
    }

    /**
     * <p>
     * 生成RSA待签名内容
     * </p>
     *
     * @param queryOrderedString 排序后的url参数
     * @param timestampValue     时间戳
     * @param nonceValue         随机数
     * @return RSA待签名内容
     * @author miaoyj
     * @since 2020-08-14
     */
    public static byte[] signData(String queryOrderedString, String timestampValue, String nonceValue) {
        /** 拼接待签名字符串 */
        StringBuilder signStrBuild = new StringBuilder(queryOrderedString);
        signStrBuild.append(timestampValue);
        signStrBuild.append(nonceValue);
        return signStrBuild.toString().getBytes();
    }

}
