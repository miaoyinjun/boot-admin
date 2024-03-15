package org.jjche.system.modules.mnt.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

import java.util.Set;

/**
 * <p>DeployDO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mnt_deploy", resultMap = "BaseResultMap")
public class DeployDO extends BaseEntity {

    @ApiModelProperty(value = "应用编号")
    private Long appId;

    @TableField(exist = false)
    private Set<ServerDeployDO> deploys;

    @TableField(exist = false)
    private AppDO app;
}
