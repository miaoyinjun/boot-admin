package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>DeptQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@Data
public class DeptQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String name;

    @QueryCriteria
    private Boolean enabled;

    @QueryCriteria
    private Long pid;

    @QueryCriteria(type = QueryCriteria.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
