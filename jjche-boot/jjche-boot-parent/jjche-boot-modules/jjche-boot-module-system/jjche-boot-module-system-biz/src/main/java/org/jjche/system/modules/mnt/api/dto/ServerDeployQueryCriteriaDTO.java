package org.jjche.system.modules.mnt.api.dto;

import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

import java.util.List;

/**
 * <p>ServerDeployQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class ServerDeployQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 模糊
     */
    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
