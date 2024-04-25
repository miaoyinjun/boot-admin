package org.jjche.common.wrapper.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.wrapper.constant.HttpStatusConstant;


/**
 * <p>
 * response包装内部错误操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class RInternalServerError<T> extends R<T> {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_UNKNOWN_ERROR
            + ":" + HttpStatusConstant.MSG_UNKNOWN_ERROR + ";",
            example = HttpStatusConstant.CODE_UNKNOWN_ERROR + "")
    private int code;

    @ApiModelProperty(example = HttpStatusConstant.MSG_UNKNOWN_ERROR + "")
    private String message;

}
