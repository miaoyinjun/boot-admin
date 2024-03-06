package org.jjche.common.abs;

import lombok.Data;

/**
 * <p>
 * response包装操作接口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-28
 */
@Data
public abstract class AbstractR {
    private int code;
    private String message;
}
