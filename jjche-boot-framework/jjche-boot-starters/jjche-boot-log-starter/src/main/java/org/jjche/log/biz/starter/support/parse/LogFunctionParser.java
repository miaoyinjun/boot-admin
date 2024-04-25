package org.jjche.log.biz.starter.support.parse;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.jjche.common.util.StrUtil;
import org.jjche.log.biz.service.IFunctionService;

import java.util.Map;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-12
 */
@AllArgsConstructor
public class LogFunctionParser {

    private IFunctionService functionService;


    public String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap, Object value, String expression, String functionName) {
        if (StrUtil.isEmpty(functionName)) {
            return value == null ? Strings.EMPTY : value.toString();
        }
        String functionReturnValue = "";
        String functionCallInstanceKey = getFunctionCallInstanceKey(functionName, expression);
        if (beforeFunctionNameAndReturnMap != null && beforeFunctionNameAndReturnMap.containsKey(functionCallInstanceKey)) {
            functionReturnValue = beforeFunctionNameAndReturnMap.get(functionCallInstanceKey);
        } else {
            functionReturnValue = functionService.apply(functionName, value);
        }
        return functionReturnValue;
    }

    /**
     * @param functionName    函数名称
     * @param paramExpression 解析前的表达式
     * @return 函数缓存的key
     * 方法执行之前换成函数的结果，此时函数调用的唯一标志：函数名+参数表达式
     */
    public String getFunctionCallInstanceKey(String functionName, String paramExpression) {
        return functionName + paramExpression;
    }


    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }


    public boolean beforeFunction(String functionName) {
        return functionService.beforeFunction(functionName);
    }
}
