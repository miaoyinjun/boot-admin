package org.jjche.sys.modules.version.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 版本
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Data
public class VersionVO implements Serializable {
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "版本号名称")
    private String name;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间", dataType = "java.lang.String")
    private Timestamp gmtCreate;
    @ApiModelProperty(value = "是否激活：0:未激活, 1:已激活")
    private Boolean isActivated;
    @ApiModelProperty(value = "创建者")
    private String createdBy;
}
