package org.jjche.core.base;

import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jjche.common.wrapper.constant.HttpStatusConstant;
import org.jjche.common.wrapper.response.RBadRequest;
import org.jjche.common.wrapper.response.RForbidden;
import org.jjche.common.wrapper.response.RInternalServerError;
import org.jjche.common.wrapper.response.RUnauthorized;
import org.springframework.validation.annotation.Validated;

@ApiResponses({
        /**增加HttpStatus.HTTP_OK，会导致200的pojo swagger说明不会生成*/
//        @ApiResponse(code = HttpStatus.HTTP_OK,
//                message = "", response = R.class),
        @ApiResponse(code = HttpStatus.HTTP_INTERNAL_ERROR,
                message = HttpStatusConstant.HTTP_INTERNAL_ERROR, response = RInternalServerError.class),
        @ApiResponse(code = HttpStatus.HTTP_BAD_REQUEST,
                message = HttpStatusConstant.HTTP_BAD_REQUEST, response = RBadRequest.class),
        @ApiResponse(code = HttpStatus.HTTP_CONFLICT,
                message = HttpStatusConstant.MSG_BUSINESS_ERROR, response = RBadRequest.class),
        @ApiResponse(code = HttpStatus.HTTP_UNAUTHORIZED,
                message = HttpStatusConstant.MSG_TOKEN_ERROR, response = RUnauthorized.class),
        @ApiResponse(code = HttpStatus.HTTP_FORBIDDEN,
                message = HttpStatusConstant.HTTP_FORBIDDEN, response = RForbidden.class)
})
@Validated
/**
 * <p>
 * 基础 控制器
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-10
 * @version 1.0.8-SNAPSHOT
 */
public class BaseController {
}
