package org.jjche.sys.modules.version.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>
 * 版本
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_version")
public class VersionDO extends BaseEntity {

    /**
     * 版本号名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否激活：0:未激活, 1:已激活
     */
    private Boolean isActivated;
}
