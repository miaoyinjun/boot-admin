package org.jjche.system.modules.mnt.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>DeployHistoryQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class DeployHistoryQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 精确
     */
    private String blurry;

    @QueryCriteria
    private Long deployId;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
