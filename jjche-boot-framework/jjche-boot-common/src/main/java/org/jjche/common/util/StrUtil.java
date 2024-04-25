package org.jjche.common.util;

import cn.hutool.core.collection.CollUtil;
import org.jjche.common.dto.LogUpdateDetailDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>StringUtils class.</p>
 *
 * @author Zheng Jie
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @version 1.0.8-SNAPSHOT
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {

    public static final String ZERO = "0";
    private static final char SEPARATOR = '_';

    /**
     * 驼峰命名法工具
     *
     * @param s a {@link java.lang.String} object.
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @param s a {@link java.lang.String} object.
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * <p>
     * 单数转换复数
     * </p>
     *
     * @param word a {@link java.lang.String} object.
     * @return 复数
     */
    public static String pluralizeWord(String word) {
        return InflectorUtil.getInstance().pluralize(word);
    }

    /**
     * <p>
     * 比较对象修改日志
     * </p>
     *
     * @param eDO  修改前
     * @param eDTO 修改后
     * @return /
     */
    public static String updateDiffByDoDto(Serializable eDO, Serializable eDTO) {
        StringBuilder detailSb = cn.hutool.core.util.StrUtil.builder();
        //保存修改前后区别字段
        List<LogUpdateDetailDTO> logUpdateDetailDTOList = ClassUtil.compareFieldsObject(eDO, eDTO);
        if (CollUtil.isNotEmpty(logUpdateDetailDTOList)) {
            for (LogUpdateDetailDTO updateDetailDTO : logUpdateDetailDTOList) {
                detailSb.append("[");
                detailSb.append("(");
                detailSb.append(updateDetailDTO.getName());
                detailSb.append(")");
                detailSb.append("，");
                detailSb.append("旧值：");
                detailSb.append("'");
                detailSb.append(updateDetailDTO.getOldVal());
                detailSb.append("'");
                detailSb.append("，");
                detailSb.append("新值：");
                detailSb.append("'");
                detailSb.append(updateDetailDTO.getNewVal());
                detailSb.append("'");
                detailSb.append("]，");
            }
        }
        return StrUtil.removeSuffix(detailSb.toString(), "，");
    }
}
