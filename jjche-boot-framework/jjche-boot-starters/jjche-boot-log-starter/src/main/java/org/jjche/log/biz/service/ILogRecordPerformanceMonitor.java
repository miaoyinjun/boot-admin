package org.jjche.log.biz.service;

import org.springframework.util.StopWatch;

public interface ILogRecordPerformanceMonitor {

    String MONITOR_NAME = "log-record-performance";
    String MONITOR_TASK_BEFORE_EXECUTE = "before-execute";
    String MONITOR_TASK_AFTER_EXECUTE = "after-execute";

    void print(StopWatch stopWatch);
}
