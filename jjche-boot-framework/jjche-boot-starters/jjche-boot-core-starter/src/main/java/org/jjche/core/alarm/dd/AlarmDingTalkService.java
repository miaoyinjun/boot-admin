package org.jjche.core.alarm.dd;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.constant.SpringPropertyConstant;
import org.jjche.core.property.CoreAlarmDingTalkProperties;
import org.jjche.core.property.CoreAlarmProperties;
import org.jjche.core.property.CoreProperties;
import org.jjche.core.util.SecurityUtil;
import org.jjche.core.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钉钉告警服务
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-12-16
 */
@Service
public class AlarmDingTalkService {

    @Autowired
    private CoreProperties coreProperties;
//    @Autowired
    private DingTalkFeignClient dingTalkFeignClient;

    /**
     * <p>
     * 发送钉钉告警
     * </p>
     *
     * @param content 内容
     * @return 是否成功
     * @author miaoyj
     * @since 2020-12-16
     */
    public boolean sendAlarm(String content) {
        try {
            CoreAlarmProperties alarm = coreProperties.getAlarm();
            CoreAlarmDingTalkProperties dingTalk = alarm.getDingTalk();
            String accessToken = dingTalk.getAccessToken();
            if (StrUtil.isNotBlank(accessToken)) {
                String userName = SecurityUtil.getUsernameOrDefaultUsername();
                String appName = SpringContextHolder.getProperties(SpringPropertyConstant.APP_NAME);
                String errorMsg = StrUtil.format("【{}】，业务告警，{}，操作人：{}", appName, content, userName);
                DingTalkContentDTO text = new DingTalkContentDTO();
                text.setContent(errorMsg);
                DingTalkDTO dingTalkDTO = new DingTalkDTO("text", text);
                ResponseEntity responseEntity = dingTalkFeignClient.send(accessToken, dingTalkDTO);
            }
        } catch (Exception e) {
            StaticLog.error("sendAlarm error:{}", e);
        }
        return true;
    }
}
