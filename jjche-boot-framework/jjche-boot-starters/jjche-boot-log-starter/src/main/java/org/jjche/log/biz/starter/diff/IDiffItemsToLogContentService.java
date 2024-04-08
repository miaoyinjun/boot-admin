package org.jjche.log.biz.starter.diff;

import de.danielbechler.diff.node.DiffNode;

/**
 * <p>
 * 对象比较
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-12
 */
public interface IDiffItemsToLogContentService {

    /**
     * <p>
     * 对象比较
     * </p>
     *
     * @param diffNode 、
     * @param o1 、
     * @param o2 、
     * @return /
     */
    String toLogContent(DiffNode diffNode, final Object o1, final Object o2);
}
