package org.jjche.sys.modules.quartz.api.enums;

/**
 * <p>
 * 定义不同主题类型
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
public enum QuartzRedisTopicEnum {

    /**
     * redis主题名称定义 需要与发布者一致
     */
    TOPIC_QUARTZ("quartz:topic", "定时任务变更Topic"),

    ;
    /**
     * 主题名称
     */
    private String topic;


    /**
     * 描述
     */
    private String description;

    QuartzRedisTopicEnum(String topic, String description) {
        this.topic = topic;
        this.description = description;
    }


    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

}
