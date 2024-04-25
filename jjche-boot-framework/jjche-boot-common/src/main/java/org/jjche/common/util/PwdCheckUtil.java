package org.jjche.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 密码校验工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2021-01-05
 */
public class PwdCheckUtil {
    /**
     * Constant <code>KEYBOARD_SLOPE_ARR</code>
     */
    public static String[] KEYBOARD_SLOPE_ARR = {
            "!qaz", "1qaz", "@wsx", "2wsx", "#edc", "3edc", "$rfv", "4rfv", "%tgb", "5tgb",
            "^yhn", "6yhn", "&ujm", "7ujm", "*ik,", "8ik,", "(ol.", "9ol.", ")p;/", "0p;/",
            "+[;.", "=[;.", "_pl,", "-pl,", ")okm", "0okm", "(ijn", "9ijn", "*uhb", "8uhb",
            "&ygv", "7ygv", "^tfc", "6tfc", "%rdx", "5rdx", "$esz", "4esz"
    };
    /**
     * Constant <code>KEYBOARD_HORIZONTAL_ARR</code>
     */
    public static String[] KEYBOARD_HORIZONTAL_ARR = {
            "01234567890-=",
            "!@#$%^&*()_+",
            "qwertyuiop[]",
            "QWERTYUIOP{}",
            "asdfghjkl;'",
            "ASDFGHJKL:",
            "zxcvbnm,./",
            "ZXCVBNM<>?",
    };

    /**
     * Constant 特殊字符
     */
    public static String SPECIAL_CHAR = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    /**
     * <p>
     * 检测密码中字符长度
     * </p>
     *
     * @param password 密码
     * @param minNum   最小长度
     * @param maxNum   最大长度
     * @return 符合长度要求 返回true
     */
    public static boolean checkPasswordLength(String password, String minNum, String maxNum) {
        boolean flag = false;
        if (StrUtil.isBlank(maxNum)) {
            minNum = StrUtil.isBlank(minNum) == true ? "0" : minNum;
            if (password.length() >= Integer.parseInt(minNum)) {
                flag = true;
            }
        } else {
            minNum = StrUtil.isBlank(minNum) == true ? "0" : minNum;
            if (password.length() >= Integer.parseInt(minNum) &&
                    password.length() <= Integer.parseInt(maxNum)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * <p>
     * 检测密码中是否包含数字
     * </p>
     *
     * @param password 密码
     * @return 包含数字 返回true
     */
    public static boolean checkContainDigit(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int numCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isDigit(chPass[i])) {
                numCount++;
            }
        }
        if (numCount >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * <p>
     * 检测密码中是否包含字母（不区分大小写）
     * </p>
     *
     * @param password 密码
     * @return 包含字母 返回true
     */
    public static boolean checkContainCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int charCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isLetter(chPass[i])) {
                charCount++;
            }
        }
        if (charCount >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * <p>
     * 检测密码中是否包含小写字母
     * </p>
     *
     * @param password 密码
     * @return 包含小写字母 返回true
     */
    public static boolean checkContainLowerCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int charCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isLowerCase(chPass[i])) {
                charCount++;
            }
        }
        if (charCount >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * <p>
     * 检测密码中是否包含大写字母
     * </p>
     *
     * @param password 密码
     * @return 包含大写字母 返回true
     */
    public static boolean checkContainUpperCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int charCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isUpperCase(chPass[i])) {
                charCount++;
            }
        }
        if (charCount >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * <p>
     * 检测密码中是否包含特殊符号
     * </p>
     *
     * @param password 密码
     * @return 包含特殊符号 返回true
     */
    public static boolean checkContainSpecialChar(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int specialCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (SPECIAL_CHAR.indexOf(chPass[i]) != -1) {
                specialCount++;
            }
        }

        if (specialCount >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * <p>
     * 键盘规则匹配器 横向连续检测
     * </p>
     *
     * @param password    密码
     * @param repetitions 重复次数
     * @param isLower     是否区分大小写
     * @return 含有横向连续字符串 返回true
     */
    public static boolean checkLateralKeyboardSite(String password, int repetitions, boolean isLower) {
        String tPassword = new String(password);
        //将所有输入字符转为小写
        tPassword = tPassword.toLowerCase();
        int n = tPassword.length();
        /**
         * 键盘横向规则检测
         */
        int arrLen = KEYBOARD_HORIZONTAL_ARR.length;
        int limitNum = repetitions;
        for (int i = 0; i + limitNum <= n; i++) {
            String str = tPassword.substring(i, i + limitNum);
            String distinguishStr = password.substring(i, i + limitNum);

            for (int j = 0; j < arrLen; j++) {
                String configStr = KEYBOARD_HORIZONTAL_ARR[j];
                String revOrderStr = new StringBuffer(KEYBOARD_HORIZONTAL_ARR[j]).reverse().toString();

                //检测包含字母(区分大小写)
                if (isLower) {
                    //考虑 大写键盘匹配的情况
                    String upperStr = KEYBOARD_HORIZONTAL_ARR[j].toUpperCase();
                    if ((configStr.indexOf(distinguishStr) != -1) || (upperStr.indexOf(distinguishStr) != -1)) {
                        return true;
                    }
                    //考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(upperStr).reverse().toString();
                    if ((revOrderStr.indexOf(distinguishStr) != -1) || (revUpperStr.indexOf(distinguishStr) != -1)) {
                        return true;
                    }
                } else {
                    if (configStr.indexOf(str) != -1) {
                        return true;
                    }
                    //考虑逆序输入情况下 连续输入
                    if (revOrderStr.indexOf(str) != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 物理键盘，斜向连接校验
     * </p>
     *
     * @param password    密码
     * @param repetitions 重复次数
     * @param isLower     是否区分大小写
     * @return 如1qaz, 4rfv, !qaz,@WDC,zaq1 返回true
     */
    public static boolean checkKeyboardSlantSite(String password, int repetitions, boolean isLower) {
        String tPassword = new String(password);
        tPassword = tPassword.toLowerCase();
        int n = tPassword.length();
        /**
         * 键盘斜线方向规则检测
         */
        int arrLen = KEYBOARD_SLOPE_ARR.length;
        int limitNum = repetitions;

        for (int i = 0; i + limitNum <= n; i++) {
            String str = tPassword.substring(i, i + limitNum);
            String distinguishStr = password.substring(i, i + limitNum);
            for (int j = 0; j < arrLen; j++) {
                String configStr = KEYBOARD_SLOPE_ARR[j];
                String revOrderStr = new StringBuffer(KEYBOARD_SLOPE_ARR[j]).reverse().toString();
                //检测包含字母(区分大小写)
                if (isLower) {
                    //考虑 大写键盘匹配的情况
                    String upperStr = KEYBOARD_SLOPE_ARR[j].toUpperCase();
                    if ((configStr.indexOf(distinguishStr) != -1) || (upperStr.indexOf(distinguishStr) != -1)) {
                        return true;
                    }
                    //考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(upperStr).reverse().toString();
                    if ((revOrderStr.indexOf(distinguishStr) != -1) || (revUpperStr.indexOf(distinguishStr) != -1)) {
                        return true;
                    }
                } else {
                    if (configStr.indexOf(str) != -1) {
                        return true;
                    }
                    //考虑逆序输入情况下 连续输入
                    if (revOrderStr.indexOf(str) != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 评估a-z,z-a这样的连续字符
     * </p>
     *
     * @param password    密码
     * @param repetitions 连续个数
     * @param isLower     是否区分大小写
     * @return 含有a-z,z-a连续字符串 返回true
     */
    public static boolean checkSequentialChars(String password, int repetitions, boolean isLower) {
        String tPassword = new String(password);
        int limitNum = repetitions;
        int normalCount = 0;
        int reversedCount = 0;
        //检测包含字母(区分大小写)
        if (!isLower) {
            tPassword = tPassword.toLowerCase();
        }
        int n = tPassword.length();
        char[] pwdCharArr = tPassword.toCharArray();

        for (int i = 0; i + limitNum <= n; i++) {
            normalCount = 0;
            reversedCount = 0;
            for (int j = 0; j < limitNum - 1; j++) {
                if (pwdCharArr[i + j + 1] - pwdCharArr[i + j] == 1) {
                    normalCount++;
                    if (normalCount == limitNum - 1) {
                        return true;
                    }
                }

                if (pwdCharArr[i + j] - pwdCharArr[i + j + 1] == 1) {
                    reversedCount++;
                    if (reversedCount == limitNum - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 验证键盘上是否存在多个连续重复的字符
     * </p>
     *
     * @param password    密码
     * @param repetitions 重复次数
     * @return 如！！！！, qqqq, 1111, ====, AAAA返回true
     */
    public static boolean checkSequentialSameChars(String password, int repetitions) {
        String tPassword = new String(password);
        int n = tPassword.length();
        char[] pwdCharArr = tPassword.toCharArray();
        int limitNum = repetitions;
        int count = 0;
        for (int i = 0; i + limitNum <= n; i++) {
            count = 0;
            for (int j = 0; j < limitNum - 1; j++) {
                if (pwdCharArr[i + j] == pwdCharArr[i + j + 1]) {
                    count++;
                    if (count == limitNum - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
