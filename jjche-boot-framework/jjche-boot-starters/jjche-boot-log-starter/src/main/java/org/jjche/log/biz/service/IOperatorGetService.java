package org.jjche.log.biz.service;


import org.jjche.log.biz.beans.Operator;

/**
 * <p>
 * 日志操作人接口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如UserContext.getCurrentUser()
     *
     * @return 转换成Operator返回
     */
    Operator getUser();
}
