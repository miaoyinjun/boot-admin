package org.jjche.system.modules.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>
 * 应用密钥
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("security_app_key")
public class SecurityAppKeyDO extends BaseEntity {
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String comment;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 应用密钥
     */
    private String appSecret;
    /**
     * 加密密钥
     */
    private String encKey;
    /**
     * 状态：1启用、0禁用
     */
    private Boolean enabled;
    /**
     * 地址
     */
    private String urls;
    /**
     * 白名单
     */
    private String whiteIp;
    /**
     * 限速（N/秒）0不限制
     */
    private Integer limitCount;
}