package org.jjche.sys.modules.quartz.task;

import cn.hutool.log.StaticLog;
import org.springframework.stereotype.Component;

/**
 * 测试用
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-08
 */
@Component
public class TestTask {

    /**
     * <p>run.</p>
     */
    public void run() {
        StaticLog.info("run 执行成功");
    }

    /**
     * <p>run1.</p>
     *
     * @param str a {@link java.lang.String} object.
     */
    public void run1(String str) {
        StaticLog.info("run1 执行成功，参数为： {}",str);
    }

    /**
     * <p>run2.</p>
     */
    public void run2() {
        StaticLog.info("run2 执行成功");
    }
}
