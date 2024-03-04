package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

/**
 * <p>DictDetailQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Data
public class DictDetailQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String label;

    @QueryCriteria
    private Long dictId;

    private String dictName;
}
