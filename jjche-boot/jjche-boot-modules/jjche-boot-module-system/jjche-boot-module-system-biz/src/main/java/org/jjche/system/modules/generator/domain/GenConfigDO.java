package org.jjche.system.modules.generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 代码生成配置
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-03
 */
@Data
@NoArgsConstructor
@TableName(value = "code_gen_config")
public class GenConfigDO implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    @NotBlank
    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "接口名称")
    private String apiAlias;
    @NotBlank
    @ApiModelProperty(value = "模块名")
    private String moduleName;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "表前缀")
    private String prefix;
    @ApiModelProperty(value = "是否覆盖")
    private Boolean cover = false;
    @ApiModelProperty(value = "版本号")
    private String apiVersion;

    /**
     * <p>Constructor for GenConfigDO.</p>
     *
     * @param tableName a {@link String} object.
     */
    public GenConfigDO(String tableName) {
        this.tableName = tableName;
    }
}
