package ${packageService}.domain;

import org.jjche.mybatis.base.entity.BaseEntityLogicDelete;
import org.jjche.mybatis.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.math.BigDecimal;
/**
* <p>
* ${apiAlias}
* </p>
*
* @author ${author}
* @since ${date}
*/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("${tableName}")
public class ${className}DO extends ${superEntityClass} {
<#if columns??>
    <#list columns as column>
    <#if column.changeColumnName != 'id' && column.changeColumnName != 'isDeleted'
        && column.changeColumnName != 'gmtCreate' && column.changeColumnName != 'gmtModified'
        && column.changeColumnName != 'createdBy' && column.changeColumnName != 'updatedBy'>
    /**
    * ${column.remark}
    */
    private ${column.columnType} ${column.changeColumnName};
    </#if>
    </#list>
</#if>
}