package org.jjche.demo.modules.student.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Data
public class StudentImportDTO {
    @NotBlank(message = "姓名不能为空")
    @Excel(name = "姓名")
    private String name;
    @NotNull(message = "年龄不能为空")
    @Excel(name = "年龄")
    private Integer age;
}
