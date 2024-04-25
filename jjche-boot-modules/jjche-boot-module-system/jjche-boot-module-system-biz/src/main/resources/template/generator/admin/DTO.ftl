package ${packageApi}.dto;

import lombok.Data;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseDTO;
import org.jjche.common.base.BaseQueryCriteriaDTO;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* ${apiAlias}
* </p>
*
* @author ${author}
* @since ${date}
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class ${className}DTO extends BaseQueryCriteriaDTO implements BaseDTO {
<#if columns??>
    <#list columns as column>
   <#if column.formShow>
   @ApiModelProperty(value = "${column.remark}"<#if column.columnType = 'Timestamp'>, dataType = "java.lang.String"</#if>)
   <#if column.istNotNull && column.columnKey != 'PRI'>
   <#if column.columnType = 'String'>
   @NotBlank(message = "${column.remark}不能为空")
   <#else>
   @NotNull(message = "${column.remark}不能为空")
   </#if>
   </#if>
   <#if column.columnKey == 'PRI'>
   @NotNull(message = "id不能为空", groups = Update.class)
   </#if>
   <#if column.maxLength?? && column.maxLength gt 0>
   @Length(max = ${column.maxLength}, message = "${column.remark}最大长度不能超过${column.maxLength}")
   </#if>
   private ${column.columnType} ${column.changeColumnName};
   </#if>
    </#list>
</#if>
}
