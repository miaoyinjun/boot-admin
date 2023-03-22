package org.jjche.system.modules.bpm.rest.admin.definition.vo.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;
import org.jjche.common.enums.CommonStatusEnum;

import java.util.List;

@ApiModel(value = "管理后台 - 用户组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class BpmUserGroupQueryDTO extends BaseQueryCriteriaDTO {

    @ApiModelProperty(value = "组名")
    @QueryCriteria(propName = "name", type = QueryCriteria.Type.LIKE)
    private String name;

    @ApiModelProperty(value = "状态")
    @QueryCriteria(propName = "status")
    private CommonStatusEnum status;

    @ApiModelProperty(value = "创建时间")
    @QueryCriteria(propName = "name", type = QueryCriteria.Type.BETWEEN)
    private List<String> createTime;

}
