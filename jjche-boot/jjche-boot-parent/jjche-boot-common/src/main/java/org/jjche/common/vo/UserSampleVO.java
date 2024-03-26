package org.jjche.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户简单字段
 * </p>
 *
 * @author miaoyj
 * @since 2023-03-22
 */
@Data
public class UserSampleVO implements Serializable {
    private Long id;
    private String nickName;
}
