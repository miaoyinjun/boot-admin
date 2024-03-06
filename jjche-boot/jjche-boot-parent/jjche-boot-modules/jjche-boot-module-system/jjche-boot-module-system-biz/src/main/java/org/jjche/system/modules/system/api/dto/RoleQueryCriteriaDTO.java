package org.jjche.system.modules.system.api.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>RoleQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * 公共查询类
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class RoleQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
