package ${packageApi}.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.base.BaseQueryCriteriaDTO;

/**
* <p>
* ${apiAlias} 查询
* </p>
*
* @author ${author}
* @since ${date}
*/
@Data
public class ${className}QueryCriteriaDTO extends BaseQueryCriteriaDTO{

<#if queryColumns??>
    <#list queryColumns as column>

<#if column.queryType = '='>
    /**
    * 精确
    */
    <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空")
    <#else>
    @NotNull(message = "${column.remark}不能为空")
    </#if>
    @ApiModelProperty(value = "${column.remark}", required = true)
    @QueryCriteria(propName = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = 'Like'>
    /**
    * 模糊
    */
    @ApiModelProperty(value = "${column.remark}", required = false)
    @QueryCriteria(propName = "${column.columnName}", type = QueryCriteria.Type.LIKE)
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '!='>
    /**
    * 不等于
    */
    <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空")
    <#else>
    @NotNull(message = "${column.remark}不能为空")
    </#if>
    @ApiModelProperty(value = "${column.remark}", required = true)
    @QueryCriteria(propName = "${column.columnName}", type = QueryCriteria.Type.NOT_EQUAL)
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = 'NotNull'>
    /**
    * 不为空
    */
    <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空")
    <#else>
    @NotNull(message = "${column.remark}不能为空")
    </#if>
    @ApiModelProperty(value = "${column.remark}", required = true)
    @QueryCriteria(propName = "${column.columnName}", type = QueryCriteria.Type.NOT_NULL)
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '>='>
    /**
    * 大于等于
    */
    <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空")
    <#else>
    @NotNull(message = "${column.remark}不能为空")
    </#if>
    @ApiModelProperty(value = "${column.remark}", required = true)
    @QueryCriteria(propName = "${column.columnName}", type = QueryCriteria.Type.GREATER_THAN)
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '<='>
    /**
    *小于等于
    */
    <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空")
    <#else>
    @NotNull(message = "${column.remark}不能为空")
    </#if>
    @ApiModelProperty(value = "${column.remark}", required = true)
    @QueryCriteria(propName = "${column.columnName}", type = QueryCriteria.Type.LESS_THAN)
    private ${column.columnType} ${column.changeColumnName};
</#if>
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
    /**
    * BETWEEN
    */
    @ApiModelProperty(value = "${column.remark}")
    @QueryCriteria(propName = "${column.columnName}", type = QueryCriteria.Type.BETWEEN)
    private List<String> ${column.changeColumnName};
    </#list>
</#if>
}
