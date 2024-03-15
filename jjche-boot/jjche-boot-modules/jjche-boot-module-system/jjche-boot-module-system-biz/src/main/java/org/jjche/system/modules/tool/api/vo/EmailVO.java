package org.jjche.system.modules.tool.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发送邮件时，接收参数的类
 *
 * @author 郑杰
 * @version 1.0.8-SNAPSHOT
 * @since 2018/09/28 12:02:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVO {

    /**
     * 收件人，支持多个收件人
     */
    @NotEmpty
    private List<String> tos;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
