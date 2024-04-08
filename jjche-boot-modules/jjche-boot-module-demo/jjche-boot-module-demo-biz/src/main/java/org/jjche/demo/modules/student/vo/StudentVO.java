package org.jjche.demo.modules.student.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;

import java.io.Serializable;
import java.sql.Timestamp;

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
public class StudentVO implements Serializable {
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "课程类型")
    private StudentCourseEnum course;
    @ApiModelProperty(value = "创建时间", dataType = "java.lang.String")
    private Timestamp gmtCreate;
    @ApiModelProperty(value = "创建者")
    private String createdBy;
}
