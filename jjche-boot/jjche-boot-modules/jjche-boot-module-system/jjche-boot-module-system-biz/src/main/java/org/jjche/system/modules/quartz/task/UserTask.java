package org.jjche.system.modules.quartz.task;

import cn.hutool.log.StaticLog;
import org.jjche.system.modules.system.service.UserService;
import org.jjche.system.property.AdminProperties;
import org.jjche.system.property.PasswordProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户任务
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-01-07
 */
@Component
public class UserTask {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminProperties adminProperties;

    /**
     * <p>
     * 用户超过N天没登录，账号过期
     * </p>
     */
    public void accountExpired() {
        Integer expiredDays = adminProperties.getUser().getAccount().getExpiredDays();
        Integer successCount = userService.updateAccountExpired(expiredDays);
        StaticLog.info("accountExpired，参数：{}，成功条数：{}", expiredDays, successCount);
    }

    /**
     * <p>
     * 用户超过N天没修改密码，密码过期
     * </p>
     */
    public void credentialsExpired() {
        PasswordProperties password = adminProperties.getUser().getPassword();
        int expiredDays = password.getExpiredDays();
        Integer successCount = userService.updateCredentialsExpired(expiredDays);
        StaticLog.info("credentialsExpired，参数：{}，成功条数：{}", expiredDays, successCount);
    }

    /**
     * <p>
     * 密码过期前N天设置必须修改密码
     * </p>
     */
    public void credentialsExpiredAdvanceDayMustReset() {
        PasswordProperties password = adminProperties.getUser().getPassword();
        int advanceDayMustResetPwd = password.getAdvanceDayMustReset();
        int expiredDays = password.getExpiredDays();
        //提前1天设置必须设置密码
        Integer successCount = userService.updateAllUserMustResetPwd(expiredDays - advanceDayMustResetPwd);
        StaticLog.info("credentialsExpiredAdvanceDayMustReset，参数：{}，成功条数：{}", expiredDays, successCount);
    }

    /**
     * <p>
     * 提前N天提醒用户修改密码
     * </p>
     */
    public void credentialsExpiredAdvanceDayTipReset() {
        PasswordProperties password = adminProperties.getUser().getPassword();
        int advanceDayTipResetPwd = password.getAdvanceDayTipReset();
        int expiredDays = password.getExpiredDays();
        //提前15天登录界面提醒用户修改密码
        Integer successCount = userService.updateUserTipResetPwd(expiredDays - advanceDayTipResetPwd);
        StaticLog.info("credentialsExpiredAdvanceDayTipReset，参数：{}，成功条数：{}", expiredDays, successCount);
    }
}
