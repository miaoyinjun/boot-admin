package org.jjche.core.exception;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;
import org.jjche.common.constant.LogConstant;
import org.jjche.common.constant.SpringPropertyConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.exception.*;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.common.wrapper.constant.HttpStatusConstant;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.util.LogUtil;
import org.jjche.core.util.SecurityUtil;
import org.jjche.core.util.SpringContextHolder;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 统一异常处理
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
//    @Autowired(required = false)
//    private CommonAPI commonAPI;

    /**
     * <p>
     * 全局异常
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R exception(Throwable e) {
        String eStr = ThrowableUtil.getStackTrace(e);
        LogRecordDTO logRecord = new LogRecordDTO();
        try {
            //已经通过@LogRecord记录了日志，这里不在记录
            if (BooleanUtil.isFalse(ContextUtil.getLogSaved())) {
                //记录到表
                String reqId = MDC.get(LogConstant.REQUEST_ID);
                String appName = SpringContextHolder.getProperties(SpringPropertyConstant.APP_NAME);
                logRecord.setModule(String.valueOf(HttpStatusConstant.CODE_UNKNOWN_ERROR));
                logRecord.setDetail(HttpStatusConstant.MSG_UNKNOWN_ERROR);
                logRecord.setSaveParams(true);
                logRecord.setOperator(SecurityUtil.getUsernameOrDefaultUsername());
                logRecord.setRequestId(reqId);
                logRecord.setExceptionDetail(eStr.getBytes());
                logRecord.setSuccess(false);
                logRecord.setAppName(appName);
                //获取请求客户端信息
                LogUtil.setLogRecordHttpRequest(logRecord);
//                commonAPI.recordLog(logRecord);
            }
        } catch (Exception ex) {

        } finally {
            if (SpringContextHolder.isDev()) {
                e.printStackTrace();
            } else {
                //e的位置为了支持CAT监控
                log.error("GlobalExceptionHandler:\n requestId:{},", logRecord.getRequestId(), e);
//                alarmDingTalkService.sendAlarm("全局异常");
            }
        }
        return R.error();
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R notFoundException(Exception e) {
        return R.notFound();
    }

    /**
     * <p>
     * 接收FeignErrorDecoderR的异常，使用fallbackFactory时不会进来
     * </p>
     *
     * @param e
    * @return /
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public R methodNotAllowedException(HttpRequestMethodNotSupportedException e) {
        e.getMethod();
        e.getSupportedMethods();
        return R.methodNotAllowed();
    }

    /**
     * <p>
     * Feign调用R异常
     * </p>
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler({FeignRException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public String feignRException(Exception e) {
        return e.getMessage();
    }

    /**
     * <p>
     * 断言验证/业务异常
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R allException(Exception e) {
        return R.validError(e.getMessage());
    }

    /**
     * <p>
     * URL路径，默认参数验证，控制器@RequestParam(required = true)
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R requestParamArgumentException(Exception e) {
        String fieldName = ((MissingServletRequestParameterException) e).getParameterName();
        String msg = StrUtil.format("{}: 不能为空", fieldName);
        return R.parameterError(msg);
    }

    /**
     * <p>
     * URL路径，自定义消息参数验证，控制器@NotBlank(message = "用户名不能为空")，...
     * </p>
     *
     * @param e a {@link javax.validation.ConstraintViolationException} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R requestParamValidatorArgumentException(ConstraintViolationException e) {
        String errorMessage = e.getMessage();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                msgBuilder.append(constraintViolation.getMessage()).append(",");
                break;
            }
            errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
        }
        return R.parameterError(errorMessage);
    }

    /**
     * <p>
     * POST @RequestBody参数验证
     * </p>
     *
     * @param e a {@link org.springframework.web.bind.MethodArgumentNotValidException} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R resolveMethodArgumentNotValidException(Exception e) {
        String errorMessage = e.getMessage();
        List<ObjectError> objectErrors = new ArrayList<>();
        if (e instanceof MethodArgumentNotValidException) {
            objectErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
        } else if (e instanceof BindException) {
            objectErrors = ((BindException) e).getBindingResult().getAllErrors();
        }
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
                break;
            }
            errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
        }
        return R.parameterError(errorMessage);
    }

    /**
     * <p>
     * 用户名或密码错误
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R userException(Exception e) {
        return R.userError();
    }

    /**
     * <p>
     * 账户已被禁用
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({DisabledException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R userDisabledException(Exception e) {
        return R.userDisabledError();
    }

    /**
     * <p>
     * 账户被锁定
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({LockedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R userLockedException(Exception e) {
        return R.userLockedError();
    }

    /**
     * <p>
     * 账户过期
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({AccountExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R usernameExpiredException(Exception e) {
        return R.userNameExpiredError();
    }

    /**
     * <p>
     * 密码过期
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({CredentialsExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R userCredentialsExpiredException(Exception e) {
        return R.userCredentialsExpiredError();
    }

    /**
     * <p>
     * 获取授权信息错误
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({AuthenticationTokenNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R authenticationTokenNotFoundException(Exception e) {
        return R.tokenNotFoundError();
    }

    /**
     * <p>authenticationTokenExpiredException.</p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     */
    @ExceptionHandler({AuthenticationTokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R authenticationTokenExpiredException(Exception e) {
        return R.tokenExpiredError();
    }

    /**
     * <p>
     * 不允许访问
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R authenticationAccessDeniedException(Exception e) {
        return R.userAccessDeniedError();
    }

    /**
     * <p>
     * 请求超时
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({RequestTimeoutException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R requestTimeoutException(Exception e) {
        return R.requestTimeout();
    }

    /**
     * <p>
     * 签名错误
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link R} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({SignException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R signException(Exception e) {
        return R.signError();
    }

    /**
     * <p>
     * 白名单限制
     * </p>
     *
     * @param e /
     * @return /
     */
    @ExceptionHandler({WhiteIpException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R whiteIpErrorException(Exception e) {
        return R.whiteIpError();
    }

    /**
     * <p>
     * 不要频繁操作
     * </p>
     *
     * @param e /
     * @return /
     */
    @ExceptionHandler({RequestLimitException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R requestLimitErrorException(Exception e) {
        return R.requestLimit();
    }
}
