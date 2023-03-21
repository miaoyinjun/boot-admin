package org.jjche.system.modules.quartz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jjche.system.modules.quartz.domain.QuartzJobDO;
import org.jjche.system.modules.quartz.enums.QuartzActionEnum;

/**
 * <p>
 * 定时器动作传输
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuartzRedisMessageDTO {
    private QuartzActionEnum action;
    private QuartzJobDO quartzJob;
}