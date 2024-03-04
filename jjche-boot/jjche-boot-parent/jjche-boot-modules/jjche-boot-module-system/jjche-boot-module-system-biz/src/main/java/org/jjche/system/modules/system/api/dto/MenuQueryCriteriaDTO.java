package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>MenuQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * 公共查询类
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class MenuQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;

    @QueryCriteria(type = QueryCriteria.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @QueryCriteria
    private Long pid;

    @QueryCriteria
    private Integer type;
}
