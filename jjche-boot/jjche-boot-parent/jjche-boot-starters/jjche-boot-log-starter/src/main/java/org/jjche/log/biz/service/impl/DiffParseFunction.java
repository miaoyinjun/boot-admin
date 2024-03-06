package org.jjche.log.biz.service.impl;

import cn.hutool.log.StaticLog;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.node.DiffNode;
import org.jjche.log.context.LogRecordContext;
import org.jjche.log.biz.diff.ArrayDiffer;
import org.jjche.log.biz.starter.diff.IDiffItemsToLogContentService;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * <p>
 * 对象比较
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-12
 */
public class DiffParseFunction {
    public static final String diffFunctionName = "_DIFF";
    public static final String OLD_OBJECT = "_oldObj";

    private static IDiffItemsToLogContentService diffItemsToLogContentService;

    //@Override
    public String functionName() {
        return diffFunctionName;
    }

    //@Override
    public String diff(Object source, Object target) {
        if (source == null && target == null) {
            return "";
        }
        if (source == null || target == null) {
            try {
                Class<?> clazz = source == null ? target.getClass() : source.getClass();
                source = source == null ? clazz.getDeclaredConstructor().newInstance() : source;
                target = target == null ? clazz.getDeclaredConstructor().newInstance() : target;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Objects.equals(source.getClass(), target.getClass())) {
            StaticLog.error("diff的两个对象类型不同, source.class={}, target.class={}", source.getClass().toString(), target.getClass().toString());
            return "";
        }
        ObjectDifferBuilder objectDifferBuilder = ObjectDifferBuilder.startBuilding();
        DiffNode diffNode = objectDifferBuilder
                .differs().register((differDispatcher, nodeQueryService) ->
                        new ArrayDiffer(differDispatcher, (ComparisonService) objectDifferBuilder.comparison(), objectDifferBuilder.identity()))
                .build()
                .compare(target, source);
        return diffItemsToLogContentService.toLogContent(diffNode, source, target);
    }

    public String diff(Object newObj) {
        Object oldObj = LogRecordContext.getVariable(OLD_OBJECT);
        return diff(oldObj, newObj);
    }

    public void setDiffItemsToLogContentService(IDiffItemsToLogContentService diffItemsToLogContentService) {
        DiffParseFunction.diffItemsToLogContentService = diffItemsToLogContentService;
    }
}
