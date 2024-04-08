package org.jjche.log.biz.starter.support;

import org.jjche.log.biz.starter.annotation.EnableLogRecord;
import org.jjche.log.biz.starter.configuration.LogRecordProxyAutoConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.lang.Nullable;

/**
 * <p>
 * 日志代理方式选择器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordConfigureSelector extends AdviceModeImportSelector<EnableLogRecord> {

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(), LogRecordProxyAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{LogRecordProxyAutoConfiguration.class.toString()};
            default:
                return null;
        }
    }
}