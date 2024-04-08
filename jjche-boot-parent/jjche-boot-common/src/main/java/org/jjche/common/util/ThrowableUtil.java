package org.jjche.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.enums.SentinelErrorInfoEnum;
import org.jjche.common.wrapper.constant.HttpStatusConstant;
import org.jjche.common.wrapper.response.R;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具 2019-01-06
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     *
     * @param throwable a {@link java.lang.Throwable} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * <p>
     * 获取Sentinel异常
     * </p>
     *
     * @param e
     * @return /
     */
    public static R getSentinelError(Throwable e) {
        Integer code = HttpStatusConstant.CODE_SENTINEL_UNKNOWN;
        String msg = HttpStatusConstant.MSG_SENTINEL_DEGRADE;
        SentinelErrorInfoEnum errorInfoEnum = SentinelErrorInfoEnum.getErrorByException(e);
        if (ObjectUtil.isNotEmpty(errorInfoEnum)) {
            code = errorInfoEnum.getCode();
            msg = errorInfoEnum.getError();
        }
        StaticLog.warn("Sentinel:{}", code);
        return R.wrap(code, msg);
    }
}
