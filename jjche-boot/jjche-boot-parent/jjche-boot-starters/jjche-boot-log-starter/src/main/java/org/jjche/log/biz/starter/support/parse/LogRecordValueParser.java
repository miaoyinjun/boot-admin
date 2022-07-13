package org.jjche.log.biz.starter.support.parse;

import cn.hutool.core.util.StrUtil;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 解析需要存储的日志里面的SpeEL表达式
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordValueParser implements BeanFactoryAware {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");
    public static final String COMMA = ",";
    private final LogRecordExpressionEvaluator expressionEvaluator = new LogRecordExpressionEvaluator();
    protected BeanFactory beanFactory;

    private LogFunctionParser logFunctionParser;

    private DiffParseFunction diffParseFunction;

    public static int strCount(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    public Map<String, String> processTemplate(Collection<String> templates, Object ret,
                                               Class<?> targetClass, Method method, Object[] args, String errorMsg,
                                               Map<String, String> beforeFunctionNameAndReturnMap) {
        Map<String, String> expressionValues = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                while (matcher.find()) {

                    String expression = matcher.group(2);
                    String functionName = matcher.group(1);
                    if (DiffParseFunction.diffFunctionName.equals(functionName)) {
                        expression = getDiffFunctionValue(evaluationContext, annotatedElementKey, expression);
                    } else {
                        Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                        expression = logFunctionParser.getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, expression, functionName);
                    }
                    matcher.appendReplacement(parsedStr, Matcher.quoteReplacement(StrUtil.nullToEmpty(expression)));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, parsedStr.toString());
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }

        }
        return expressionValues;
    }

    public Map<String, List<String>> parseBatchTemplate(Collection<String> templates, Object ret,
                                                        Class<?> targetClass, Method method, Object[] args, String errorMsg,
                                                        Map<String, String> beforeFunctionNameAndReturnMap, String bizNoTemplate) {
        Map<String, List<String>> expressionValues = new HashMap<>(16);
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);
        Object bizNoObj = bizNoTemplate;
        if (bizNoTemplate.contains("{")) {
            Matcher matcher = pattern.matcher(bizNoTemplate);
            if (matcher.find()) {
                String expression = matcher.group(2);
                AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                bizNoObj = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
            }
        }
        int length = bizNoObj instanceof List ? ((List<?>) bizNoObj).size() : 1;
        for (String expressionTemplate : templates) {
            for (int i = 0; i < length; i++) {
                List<String> values = new ArrayList<>(16);
                if (expressionTemplate.contains("{")) {
                    Matcher matcher = pattern.matcher(expressionTemplate);
                    StringBuffer parsedStr = new StringBuffer();
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    while (matcher.find()) {
                        String expression = matcher.group(2);
                        String functionName = matcher.group(1);
                        if (DiffParseFunction.diffFunctionName.equals(functionName)) {
                            expression = getDiffFunctionValue(evaluationContext, annotatedElementKey, expression, i);
                        } else {
                            Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                            if (value instanceof List) {
                                List<?> val = (List<?>) value;
                                if (val.size() > i) {
                                    expression = logFunctionParser.getFunctionReturnValue(beforeFunctionNameAndReturnMap, val.get(i), expression, functionName);
                                } else {
                                    expression = logFunctionParser.getFunctionReturnValue(beforeFunctionNameAndReturnMap, val.get(val.size() - 1), expression, functionName);
                                }
                            } else {
                                expression = logFunctionParser.getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, expression, functionName);
                            }
                        }
                        matcher.appendReplacement(parsedStr, Matcher.quoteReplacement(StrUtil.nullToEmpty(expression)));
                    }
                    matcher.appendTail(parsedStr);
                    values.add(parsedStr.toString());
                } else {
                    values.add(expressionTemplate);
                }
                expressionValues.merge(expressionTemplate, values, (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
        return expressionValues;
    }

    private String getDiffFunctionValue(EvaluationContext evaluationContext, AnnotatedElementKey annotatedElementKey, String expression) {
        String[] params = parseDiffFunction(expression);
        if (params.length == 1) {
            Object targetObj = expressionEvaluator.parseExpression(params[0], annotatedElementKey, evaluationContext);
            expression = diffParseFunction.diff(targetObj);
        } else if (params.length == 2) {
            Object sourceObj = expressionEvaluator.parseExpression(params[0], annotatedElementKey, evaluationContext);
            Object targetObj = expressionEvaluator.parseExpression(params[1], annotatedElementKey, evaluationContext);
            expression = diffParseFunction.diff(sourceObj, targetObj);
        }
        return expression;
    }

    private String getDiffFunctionValue(EvaluationContext evaluationContext, AnnotatedElementKey annotatedElementKey, String expression, int index) {
        String[] params = parseDiffFunction(expression);
        if (params.length == 1) {
            Object targetObj = expressionEvaluator.parseExpression(params[0], annotatedElementKey, evaluationContext);
            if (targetObj instanceof List) {
                List<?> targetObjList = (List<?>) targetObj;
                if (targetObjList.size() <= index) {
                    expression = diffParseFunction.diff(targetObjList.get(targetObjList.size() - 1), index);
                } else {
                    expression = diffParseFunction.diff(targetObjList.get(index), index);
                }
            } else {
                expression = diffParseFunction.diff(targetObj, index);
            }
        } else if (params.length == 2) {
            Object sourceObj = expressionEvaluator.parseExpression(params[0], annotatedElementKey, evaluationContext);
            Object targetObj = expressionEvaluator.parseExpression(params[1], annotatedElementKey, evaluationContext);
            if (sourceObj instanceof List && !(targetObj instanceof List)) {
                List<?> sourceObjList = (List<?>) sourceObj;
                if (sourceObjList.size() <= index) {
                    expression = diffParseFunction.diff(sourceObjList.get(sourceObjList.size() - 1), targetObj);
                } else {
                    expression = diffParseFunction.diff(sourceObjList.get(index), targetObj);
                }
            } else if (!(sourceObj instanceof List) && targetObj instanceof List) {
                List<?> targetObjList = (List<?>) targetObj;
                if (targetObjList.size() <= index) {
                    expression = diffParseFunction.diff(sourceObj, targetObjList.get(targetObjList.size() - 1));
                } else {
                    expression = diffParseFunction.diff(sourceObj, targetObjList.get(index));
                }
            } else if (sourceObj instanceof List && targetObj instanceof List) {
                List<?> sourceObjList = (List<?>) sourceObj;
                List<?> targetObjList = (List<?>) targetObj;
                int srcIndex = sourceObjList.size() > index ? index : sourceObjList.size() - 1;
                int targetIndex = targetObjList.size() > index ? index : targetObjList.size() - 1;
                expression = diffParseFunction.diff(sourceObjList.get(srcIndex), targetObjList.get(targetIndex));
            } else {
                expression = diffParseFunction.diff(sourceObj, targetObj);
            }
        }
        return expression;
    }

    private String[] parseDiffFunction(String expression) {
        if (expression.contains(COMMA) && strCount(expression, COMMA) == 1) {
            return expression.split(COMMA);
        }
        return new String[]{expression};
    }

    public Map<String, String> processBeforeExecuteFunctionTemplate(Collection<String> templates, Class<?> targetClass, Method method, Object[] args) {
        Map<String, String> functionNameAndReturnValueMap = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, null, null, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    if (expression.contains("#_ret") || expression.contains("#_errorMsg")) {
                        continue;
                    }
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String functionName = matcher.group(1);
                    if (logFunctionParser.beforeFunction(functionName)) {
                        Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                        String functionReturnValue = logFunctionParser.getFunctionReturnValue(null, value, expression, functionName);
                        String functionCallInstanceKey = logFunctionParser.getFunctionCallInstanceKey(functionName, expression);
                        functionNameAndReturnValueMap.put(functionCallInstanceKey, functionReturnValue);
                    }
                }
            }
        }
        return functionNameAndReturnValueMap;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setLogFunctionParser(LogFunctionParser logFunctionParser) {
        this.logFunctionParser = logFunctionParser;
    }

    public void setDiffParseFunction(DiffParseFunction diffParseFunction) {
        this.diffParseFunction = diffParseFunction;
    }
}
