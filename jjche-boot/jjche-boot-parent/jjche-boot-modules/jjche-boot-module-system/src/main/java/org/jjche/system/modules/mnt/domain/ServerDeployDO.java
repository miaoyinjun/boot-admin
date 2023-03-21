package org.jjche.system.modules.mnt.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>ServerDeployDO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mnt_server")
public class ServerDeployDO extends BaseEntity {

    @ApiModelProperty(value = "服务器名称")
    private String name;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "端口")
    private Integer port;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;
}
