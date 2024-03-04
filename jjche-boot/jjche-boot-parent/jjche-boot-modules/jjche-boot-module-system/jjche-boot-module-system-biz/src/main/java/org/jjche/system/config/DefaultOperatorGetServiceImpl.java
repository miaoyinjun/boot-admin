package org.jjche.system.config;


import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.beans.Operator;
import org.jjche.log.biz.service.IOperatorGetService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 日志当前用户
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-29
 */
@Component
@Configuration
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId(SecurityUtil.getUsernameOrDefaultUsername());
        return operator;
    }
}
