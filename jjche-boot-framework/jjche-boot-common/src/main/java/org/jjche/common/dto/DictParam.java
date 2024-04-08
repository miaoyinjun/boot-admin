package org.jjche.common.dto;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 字典入参
 * </p>
 *
 * @author miaoyj
 * @since 2022-09-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictParam implements Serializable {
    /**
     * 值
     */
    @EnumValue
    private String value;
    /**
     * 描述
     */
    private String label;
}
