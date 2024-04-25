package org.jjche.log.biz.service.impl;


import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.beans.Operator;
import org.jjche.log.biz.service.IOperatorGetService;

/**
 * <p>
 * 缺省操作人
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
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
