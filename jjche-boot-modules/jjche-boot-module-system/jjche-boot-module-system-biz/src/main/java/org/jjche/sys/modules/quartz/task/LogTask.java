package org.jjche.sys.modules.quartz.task;

import cn.hutool.core.util.NumberUtil;
import org.jjche.sys.modules.logging.service.LogService;
import org.jjche.sys.modules.quartz.service.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 日志任务
 * </p>
 *
 * @author miaoyj
 * @since 2021-12-13
 */
@Component
public class LogTask {

    @Autowired
    private LogService logService;
    @Autowired
    private QuartzJobService quartzJobService;

    /**
     * <p>
     * 清空N个月之前的操作日志
     * </p>
     *
     * @param month 月
     */
    public void cleanLogs(String month) {
        Integer monthInt = NumberUtil.parseInt(month);
        logService.delMonth(monthInt);
        quartzJobService.delMonth(monthInt);
    }

}
