package org.jjche.system.modules.mnt.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>DeployHistoryDO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mnt_deploy_history")
public class DeployHistoryDO extends BaseEntity {

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "部署者")
    private String deployUser;

    @ApiModelProperty(value = "部署ID")
    private Long deployId;
}
