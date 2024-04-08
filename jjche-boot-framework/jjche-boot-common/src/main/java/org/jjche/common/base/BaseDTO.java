package org.jjche.common.base;

import java.io.Serializable;

/**
 * <p>
 * DTO基类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-25
 */
public interface BaseDTO extends Serializable {

    /* 分组创建校验 */
    @interface Create {
    }

    /* 分组修改校验 */
    @interface Update {
    }
}
