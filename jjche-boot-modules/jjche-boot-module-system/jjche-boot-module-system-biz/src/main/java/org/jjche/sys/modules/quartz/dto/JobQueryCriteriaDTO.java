package org.jjche.sys.modules.quartz.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>JobQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-4 10:33:02
 */
@Data
public class JobQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String jobName;

    @QueryCriteria
    private Boolean isSuccess;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
