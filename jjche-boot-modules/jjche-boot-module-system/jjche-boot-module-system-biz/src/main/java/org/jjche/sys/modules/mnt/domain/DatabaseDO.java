package org.jjche.sys.modules.mnt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>DatabaseDO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mnt_database")
public class DatabaseDO extends BaseEntity {

    @ApiModelProperty(value = "数据库名称")
    private String name;

    @ApiModelProperty(value = "数据库连接地址")
    private String jdbcUrl;

    @ApiModelProperty(value = "数据库密码")
    private String pwd;

    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * <p>copy.</p>
     *
     * @param source a {@link DatabaseDO} object.
     */
    public void copy(DatabaseDO source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
