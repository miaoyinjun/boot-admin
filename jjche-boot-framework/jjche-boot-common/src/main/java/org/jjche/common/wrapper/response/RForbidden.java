package org.jjche.common.wrapper.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.wrapper.constant.HttpStatusConstant;


/**
 * <p>
 * response包装未授权操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class RForbidden<T> extends R<T> {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_TOKEN_EXPIRED
            + ":" + HttpStatusConstant.MSG_TOKEN_EXPIRED + ";"
            + HttpStatusConstant.CODE_USER_ACCESS_DENIED
            + ":" + HttpStatusConstant.MSG_USER_ACCESS_DENIED + ";"
            ,
            example = HttpStatusConstant.CODE_TOKEN_EXPIRED + "")
    private int code;

    @ApiModelProperty(example = HttpStatusConstant.MSG_TOKEN_EXPIRED + "")
    private String message;

}
