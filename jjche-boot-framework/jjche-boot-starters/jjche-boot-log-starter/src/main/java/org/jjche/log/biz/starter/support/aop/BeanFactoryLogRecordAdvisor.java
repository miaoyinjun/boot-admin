package org.jjche.log.biz.starter.support.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * <p>
 * 日志配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class BeanFactoryLogRecordAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final LogRecordPointcut pointcut = new LogRecordPointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        pointcut.setLogRecordOperationSource(logRecordOperationSource);
    }
}
