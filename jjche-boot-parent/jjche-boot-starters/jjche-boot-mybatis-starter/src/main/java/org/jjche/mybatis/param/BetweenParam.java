package org.jjche.mybatis.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Between参数
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetweenParam {
    @ApiModelProperty(value = "开始值")
    private String start;
    @ApiModelProperty(value = "结束值")
    private String end;
}
