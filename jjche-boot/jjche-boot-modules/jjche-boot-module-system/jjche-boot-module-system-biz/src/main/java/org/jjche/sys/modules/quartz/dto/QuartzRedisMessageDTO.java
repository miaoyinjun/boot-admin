package org.jjche.sys.modules.quartz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jjche.sys.modules.quartz.enums.QuartzActionEnum;
import org.jjche.sys.modules.quartz.domain.QuartzJobDO;

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
