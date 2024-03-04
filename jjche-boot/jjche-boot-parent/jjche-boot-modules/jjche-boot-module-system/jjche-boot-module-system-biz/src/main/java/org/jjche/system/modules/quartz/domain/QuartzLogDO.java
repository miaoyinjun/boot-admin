package org.jjche.system.modules.quartz.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>QuartzLogDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Data
@NoArgsConstructor
@TableName("sys_quartz_log")
public class QuartzLogDO implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @OrderBy
    private Long id;

    /**
     * 创建时间
     */
    private Timestamp gmtCreate;

    @ApiModelProperty(value = "任务名称", hidden = true)
    private String jobName;

    @ApiModelProperty(value = "bean名称", hidden = true)
    private String beanName;

    @ApiModelProperty(value = "方法名称", hidden = true)
    private String methodName;

    @ApiModelProperty(value = "参数", hidden = true)
    private String params;

    @ApiModelProperty(value = "cron表达式", hidden = true)
    private String cronExpression;

    @ApiModelProperty(value = "状态", hidden = true)
    private Boolean isSuccess;

    @ApiModelProperty(value = "异常详情", hidden = true)
    private String exceptionDetail;

    @ApiModelProperty(value = "执行耗时", hidden = true)
    private Long time;
}
