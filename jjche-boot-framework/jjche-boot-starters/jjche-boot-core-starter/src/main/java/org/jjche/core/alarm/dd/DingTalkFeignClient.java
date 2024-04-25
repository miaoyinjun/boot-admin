package org.jjche.core.alarm.dd;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 钉钉
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-12-16
 */
//@FeignClient(name = "ALARM-DING-TALK", url = "${" + CoreConstant.PROPERTY_CORE_ALARM_DING_TALK_URL_PACKAGE_PREFIX + "}")
public interface DingTalkFeignClient {
    /**
     * <p>send.</p>
     *
     * @param accessToken a {@link java.lang.String} object.
     * @param dingTalkDTO a {@link DingTalkDTO} object.
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @PostMapping
    ResponseEntity send(@RequestParam("access_token") String accessToken, @RequestBody DingTalkDTO dingTalkDTO);

}