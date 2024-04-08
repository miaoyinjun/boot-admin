package org.jjche.log.biz.starter.support.parse;

import org.jjche.log.context.LogRecordContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * 日志表达式变量设置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {

    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                      ParameterNameDiscoverer parameterNameDiscoverer, Object ret, String errorMsg) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        Map<String, Object> variables = LogRecordContext.getVariables();
        Map<String, Object> globalVariable = LogRecordContext.getGlobalVariableMap();
        if (variables != null) {
            setVariables(variables);
        }
        if (globalVariable != null && !globalVariable.isEmpty()) {
            for (Map.Entry<String, Object> entry : globalVariable.entrySet()) {
                if (lookupVariable(entry.getKey()) == null) {
                    setVariable(entry.getKey(), entry.getValue());
                }
            }
        }
        setVariable("_ret", ret);
        setVariable("_errorMsg", errorMsg);
    }
}
