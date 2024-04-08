package org.jjche.sys.modules.quartz.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import lombok.AllArgsConstructor;
import org.jjche.cache.conf.RedisSubscriber;
import org.jjche.sys.modules.quartz.enums.QuartzRedisTopicEnum;
import org.jjche.sys.modules.quartz.domain.QuartzJobDO;
import org.jjche.sys.modules.quartz.dto.QuartzRedisMessageDTO;
import org.jjche.sys.modules.quartz.enums.QuartzActionEnum;
import org.jjche.sys.modules.quartz.utils.QuartzManage;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 定时器的的订阅者
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
@Component
@AllArgsConstructor
public class QuartzSubscriber implements RedisSubscriber {

    private final QuartzManage quartzManage;
    @Override
    public String getTopic() {
        return QuartzRedisTopicEnum.TOPIC_QUARTZ.getTopic();
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();
        StaticLog.debug(">> 定时器接收消息：{}", msg);
        QuartzRedisMessageDTO dto = JSONUtil.toBean(msg, QuartzRedisMessageDTO.class);
        QuartzActionEnum action = dto.getAction();
        QuartzJobDO quartzJob = dto.getQuartzJob();
        switch (action) {
            case ADD:
                quartzManage.addJob(quartzJob);
                break;
            case UPDATE:
                quartzManage.updateJobCron(quartzJob);
                break;
            case PAUSE:
                if (quartzJob.getIsPause()) {
                    quartzManage.resumeJob(quartzJob);
                } else {
                    quartzManage.pauseJob(quartzJob);
                }
                break;
            case EXEC:
                quartzManage.runJobNow(quartzJob);
                break;
            case DEL:
                quartzManage.deleteJob(quartzJob);
                break;
            default:
                break;
        }
    }

}
