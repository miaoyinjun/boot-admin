package ${packageApi}.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
* <p>
* ${apiAlias}
* </p>
*
* @author ${author}
* @since ${date}
*/
@Data
public class ${className}VO implements Serializable {
<#if columns??>
    <#list columns as column>
   <#if column.columnShow>
   @ApiModelProperty(value = "${column.remark}"<#if column.columnType = 'Timestamp'>, dataType = "java.lang.String"</#if>)
   private ${column.columnType} ${column.changeColumnName};
   </#if>
    </#list>
</#if>
}