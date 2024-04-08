package org.jjche.sys.modules.system.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>UserQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Data
public class UserQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria
    private Long id;

    @QueryCriteria(propName = "dept_id", type = QueryCriteria.Type.IN)
    private Set<Long> deptIds = new HashSet<>();

    private String blurry;

    @QueryCriteria
    private Boolean enabled;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;

    private Long deptId;
}
