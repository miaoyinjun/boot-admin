package com.boot.admin.tool.modules.tool.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 邮件配置类，数据存覆盖式存入数据存
 *
 * @author Zheng Jie
 * @since 2018-12-26
 * @version 1.0.8-SNAPSHOT
 */
@Data
@NoArgsConstructor
@TableName(value = "tool_email_config")
public class EmailConfigDO extends BaseEntity {

    @NotBlank
    @ApiModelProperty(value = "邮件服务器SMTP地址")
    private String host;

    @NotBlank
    @ApiModelProperty(value = "邮件服务器 SMTP 端口")
    private String port;

    @NotBlank
    @ApiModelProperty(value = "发件者用户名")
    private String user;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String pass;

    @NotBlank
    @ApiModelProperty(value = "收件人")
    private String fromUser;
}
