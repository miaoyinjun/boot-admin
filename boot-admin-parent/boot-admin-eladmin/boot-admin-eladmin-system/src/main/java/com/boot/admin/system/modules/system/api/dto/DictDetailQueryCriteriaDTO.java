package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import lombok.Data;

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
