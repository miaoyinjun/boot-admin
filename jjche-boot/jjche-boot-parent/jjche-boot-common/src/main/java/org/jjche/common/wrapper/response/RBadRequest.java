package org.jjche.common.wrapper.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.wrapper.constant.HttpStatusConstant;


/**
 * <p>
 * response包装验证错误操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class RBadRequest<T> extends R<T> {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_PARAMETER_ERROR
            + ":" + HttpStatusConstant.MSG_PARAMETER_ERROR + ";"
//            + HttpStatusConstant.CODE_VALID_ERROR
//            + ":" + HttpStatusConstant.MSG_VALID_ERROR + ";"
            + HttpStatusConstant.CODE_REQUEST_TIMEOUT
            + ":" + HttpStatusConstant.MSG_REQUEST_TIMEOUT + ";"
            + HttpStatusConstant.CODE_SIGN_ERROR
            + ":" + HttpStatusConstant.MSG_SIGN_ERROR + ";"
            + HttpStatusConstant.CODE_REQUEST_LIMIT
            + ":" + HttpStatusConstant.MSG_REQUEST_LIMIT + ";"

            + HttpStatusConstant.CODE_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS
            + ":" + HttpStatusConstant.MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS + ";"
            + HttpStatusConstant.CODE_USER_DISABLED
            + ":" + HttpStatusConstant.MSG_USER_DISABLED + ";"
            + HttpStatusConstant.CODE_USER_LOCKED
            + ":" + HttpStatusConstant.MSG_USER_LOCKED + ";"
            + HttpStatusConstant.CODE_USERNAME_EXPIRED
            + ":" + HttpStatusConstant.MSG_USERNAME_EXPIRED + ";"
            + HttpStatusConstant.CODE_USER_CREDENTIALS_EXPIRED
            + ":" + HttpStatusConstant.MSG_USER_CREDENTIALS_EXPIRED + ";"
            ,
            example = HttpStatusConstant.CODE_PARAMETER_ERROR + "")
    private int code;

    @ApiModelProperty(example = HttpStatusConstant.MSG_PARAMETER_ERROR + "")
    private String message;

}
