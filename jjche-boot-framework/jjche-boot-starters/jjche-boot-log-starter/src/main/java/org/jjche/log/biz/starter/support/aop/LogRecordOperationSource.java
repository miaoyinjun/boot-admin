package org.jjche.log.biz.starter.support.aop;

import org.jjche.common.dto.LogRecordDTO;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * <p>
 * 配置日志注解与日志记录关系
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordOperationSource {

    public Collection<LogRecordDTO> computeLogRecordOperations(Method method, Class<?> targetClass) {
        // Don't allow no-public methods as required.
        if (!Modifier.isPublic(method.getModifiers())) {
            return Collections.emptyList();
        }

        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        Collection<LogRecordDTO> logRecordOps = parseLogRecordAnnotations(specificMethod);
        Collection<LogRecordDTO> abstractLogRecordOps = parseLogRecordAnnotations(ClassUtils.getInterfaceMethodIfPossible(method));
        HashSet<LogRecordDTO> result = new HashSet<>();
        result.addAll(logRecordOps);
        result.addAll(abstractLogRecordOps);
        return result;
    }

    private Collection<LogRecordDTO> parseLogRecordAnnotations(AnnotatedElement ae) {
        Collection<LogRecord> logRecordAnnotationAnnotations = AnnotatedElementUtils.findAllMergedAnnotations(ae, LogRecord.class);
        Collection<LogRecordDTO> ret = new ArrayList<>();
        if (!logRecordAnnotationAnnotations.isEmpty()) {
            for (LogRecord recordAnnotation : logRecordAnnotationAnnotations) {
                ret.add(parseLogRecordAnnotation(ae, recordAnnotation));
            }
        }
        return ret;
    }

    /**
     * <p>
     * 转换日志注解与日志对象
     * </p>
     *
     * @param ae               日志注解元素
     * @param recordAnnotation 日志注解
     * @return 日志对象
     */
    private LogRecordDTO parseLogRecordAnnotation(AnnotatedElement ae, LogRecord recordAnnotation) {
        String bizKey = recordAnnotation.prefix();
        String bizNo = recordAnnotation.bizNo();
        LogRecordDTO recordOps = LogRecordDTO.builder().bizKey(bizKey).bizNo(bizNo).operatorId(recordAnnotation.operatorId()).category(recordAnnotation.category()).value(recordAnnotation.value()).type(recordAnnotation.type()).module(recordAnnotation.module()).subModule(recordAnnotation.subModule()).detail(recordAnnotation.detail()).condition(recordAnnotation.condition()).saveParams(recordAnnotation.saveParams()).build();
        return recordOps;
    }


}
