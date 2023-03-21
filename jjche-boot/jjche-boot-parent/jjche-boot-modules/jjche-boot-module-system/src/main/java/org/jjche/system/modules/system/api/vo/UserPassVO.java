package org.jjche.system.modules.system.api.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019年7月11日13:59:49
 */
@Data
public class UserPassVO {

    private String oldPass;

    private String newPass;
}
