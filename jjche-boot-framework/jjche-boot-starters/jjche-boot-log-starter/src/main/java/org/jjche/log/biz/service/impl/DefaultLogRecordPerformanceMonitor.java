package org.jjche.log.biz.service.impl;

import cn.hutool.log.StaticLog;
import org.jjche.log.biz.service.ILogRecordPerformanceMonitor;
import org.springframework.util.StopWatch;

/**
 * @author muzhantong
 * create on 2022/7/17 10:49 PM
 */
public class DefaultLogRecordPerformanceMonitor implements ILogRecordPerformanceMonitor {

    @Override
    public void print(StopWatch stopWatch) {
        StaticLog.debug("LogRecord performance={}", stopWatch.prettyPrint());
    }
}
