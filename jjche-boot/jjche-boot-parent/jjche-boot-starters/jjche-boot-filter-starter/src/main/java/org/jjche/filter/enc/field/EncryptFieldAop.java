package org.jjche.filter.enc.field;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jjche.filter.enc.field.enums.EncryptField;
import org.jjche.filter.enc.field.enums.EncryptMethod;
import org.jjche.common.util.ThrowableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * <p>
 * 安全字段加密解密切面
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-08-25
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
public class EncryptFieldAop {

    @Autowired
    private EncryptFieldUtil encryptFieldUtil;

    /**
     * <p>annotationPointCut.</p>
     */
    @Pointcut("@annotation(org.jjche.filter.enc.field.enums.EncryptMethod)")
    public void annotationPointCut() {
    }

    /**
     * <p>around.</p>
     *
     * @param joinPoint a {@link org.aspectj.lang.ProceedingJoinPoint} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object responseObj = null;
        Throwable throwable = null;
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
            Annotation[][] parameterAnnotations = realMethod.getParameterAnnotations();
            Object[] args = joinPoint.getArgs();
            //是否存在参数注解
            if (ArrayUtil.isNotEmpty(parameterAnnotations) && ArrayUtil.isNotEmpty(args)) {
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] parameterAnnotation = parameterAnnotations[i];
                    //参数是否包含加密注解
                    boolean isParamEncryptField = ArrayUtil.isNotEmpty(parameterAnnotation) && ClassUtil.isAssignable(EncryptField.class, parameterAnnotation[0].getClass());
                    if (isParamEncryptField) {
                        Object arg = args[i];
                        args[i] = handleEncrypt(arg, EncryptFieldType.DECRYPT);
                    }
                }
            }
            responseObj = joinPoint.proceed(args);
            EncryptMethod encryptMethod = realMethod.getAnnotation(EncryptMethod.class);
            //是否加密返回数据
            if (encryptMethod != null && encryptMethod.returnVal()) {
                responseObj = handleEncrypt(responseObj, EncryptFieldType.ENCRYPT);
            }
        } catch (Exception t) {
            throwable = t;
            //记录日志错误不要影响业务
            StaticLog.error("log encryptField parse exception:{}", ThrowableUtil.getStackTrace(t));
        }
        if (throwable != null) {
            throw throwable;
        }
        return responseObj;
    }

    /**
     * <p>
     * 处理出参加密，入参解密
     * </p>
     *
     * @param requestObj 出参/入参
     * @return /
     */
    private Object handleEncrypt(Object requestObj, EncryptFieldType encryptFieldType) throws IllegalAccessException {
        if (Objects.isNull(requestObj)) {
            return null;
        }
        Class myPageClass = ClassUtil.loadClass("org.jjche.mybatis.param.MyPage");
        boolean isMyPage = ClassUtil.isAssignable(myPageClass, requestObj.getClass());
        //MyPage出参特殊处理
        if (isMyPage && encryptFieldType == EncryptFieldType.ENCRYPT) {
            Object myPage = Convert.convert(myPageClass, requestObj);
            encryptFieldUtil.setEncryptMyPageRecords(myPage);
        } else {
            boolean isStr = ClassUtil.isAssignable(String.class, requestObj.getClass());
            boolean isCollection = ClassUtil.isAssignable(Collection.class, requestObj.getClass());
            boolean isCollectionStr = false;
            boolean isCollectionPojo = false;
            if (isCollection) {
                Predicate<Object> strPredicate = new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) {
                        return ClassUtil.isAssignable(String.class, o.getClass());
                    }
                };
                Predicate<Object> pojoPredicate = new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) {
                        return !ClassUtil.isBasicType(o.getClass());
                    }
                };
                isCollectionStr = CollUtil.contains((Collection) requestObj, strPredicate);
                isCollectionPojo = CollUtil.contains((Collection) requestObj, pojoPredicate);
            }
            boolean isPojo = !ClassUtil.isBasicType(requestObj.getClass()) && !isCollectionPojo;

            //参数类型是String
            if (isStr) {
                String encryptValue = StrUtil.toString(requestObj);
                requestObj = encryptFieldUtil.handleEncryptStr(encryptValue, encryptFieldType);
            }//参数类型是Collection<String>
            else if (isCollectionStr) {
                List<String> strList = Convert.toList(String.class, requestObj);
                requestObj = encryptFieldUtil.handleEncryptCollectionStr(strList, encryptFieldType);
            }
            //参数类型是POJO
            else if (isPojo) {
                encryptFieldUtil.setObjectFieldEncrypt(requestObj, encryptFieldType);
            }//参数类型是Collection<POJO>
            else if (isCollectionPojo) {
                List<Object> pojoList = Convert.toList(Object.class, requestObj);
                encryptFieldUtil.setCollectionObjectFieldEncrypt(pojoList, encryptFieldType);
                requestObj = pojoList;
            }
        }
        return requestObj;
    }

}
