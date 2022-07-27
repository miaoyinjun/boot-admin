package org.jjche.demo.modules.student.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseDTO;
import org.jjche.common.dto.BaseQueryCriteriaDTO;
import org.jjche.demo.modules.student.api.enums.CourseEnum;

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
public class StudentDTO extends BaseQueryCriteriaDTO implements BaseDTO {
    @NotNull(message = "id不能为空", groups = Update.class)
    @QueryCriteria(propName = "id", type = QueryCriteria.Type.EQUAL)
    private Long id;
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", access = "")
    private String name;
    @NotNull(message = "年龄不能为空")
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "课程类型")
    private CourseEnum course;
    @ApiModelProperty(value = "所属用户id")
    private Long creatorUserId;
}
