package org.jjche.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>DeptSmallDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-10 16:32:18
 */
@Data
public class DeptSmallDto implements Serializable {

    private Long id;

    private String name;

    /**
     * 负责人的用户编号
     */
    private Long leaderUserId;

    private Long pid;
    /**
     * <p>Constructor for DeptSmallDto.</p>
     */
    public DeptSmallDto() {
    }
}
