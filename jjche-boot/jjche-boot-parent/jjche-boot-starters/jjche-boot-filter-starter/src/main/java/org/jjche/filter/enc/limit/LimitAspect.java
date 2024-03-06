package org.jjche.filter.enc.limit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jjche.cache.service.RedisService;
import org.jjche.common.exception.RequestLimitException;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.filter.constant.FilterCacheKey;
import org.jjche.filter.enc.limit.annotation.Limit;
import org.jjche.filter.enc.limit.enums.LimitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * <p>LimitAspect class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedisService redisService;

    /**
     * <p>pointcut.</p>
     */
    @Pointcut("@annotation(org.jjche.filter.enc.limit.annotation.Limit)")
    public void pointcut() {
    }

    /**
     * <p>around.</p>
     *
     * @param joinPoint a {@link org.aspectj.lang.ProceedingJoinPoint} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StrUtil.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = HttpUtil.getIp(request);
            } else {
                key = signatureMethod.getName();
            }
        }

        String url = request.getRequestURI().replaceAll("/", "_");
        List<Object> keys = CollUtil.toList(StrUtil.join(limit.prefix(), "_", key, "_", url));

        RedisScript<Long> redisScript = new DefaultRedisScript<>(FilterCacheKey.SCRIPT_LUA_LIMIT, Long.class);
        Number count = redisService.execute(redisScript, keys, limit.count(), limit.period());
        Boolean isMorThan = null != count && count.intValue() <= limit.count();
        if (BooleanUtil.isFalse(isMorThan)) {
            throw new RequestLimitException();
        }
        StaticLog.debug("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
        return joinPoint.proceed();
    }
}
