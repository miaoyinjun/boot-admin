package org.jjche.system.modules.version.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

/**
 * <p>
 * 版本 查询
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Data
public class VersionQueryCriteriaDTO extends BaseQueryCriteriaDTO {


    /**
     * 模糊
     */
    @ApiModelProperty(value = "版本号名称", required = false)
    @QueryCriteria(propName = "name", type = QueryCriteria.Type.LIKE)
    private String name;
}
