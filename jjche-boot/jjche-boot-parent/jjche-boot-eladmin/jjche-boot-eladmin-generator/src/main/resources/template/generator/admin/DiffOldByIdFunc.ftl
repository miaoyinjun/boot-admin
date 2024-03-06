package ${packageService}.function;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import ${packageService}.domain.${className}DO;
import ${packageService}.mapstruct.${className}MapStruct;
import ${packageService}.service.${className}Service;
import org.jjche.log.context.LogRecordContext;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * <p>
 * ${apiAlias} 设置修改/删除前的数据到变量
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Component
@RequiredArgsConstructor
public class ${className}DiffOldByIdParseFunction implements IParseFunction<Object> {
    private final ${className}Service ${changeClassName}Service;
    private final ${className}MapStruct ${changeClassName}MapStruct;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeBefore() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String functionName() {
        return "${diffOldFuncName}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Object idObj) {
        Object result = null;
        if (idObj instanceof List) {
            List<Long> ids = (List<Long>) idObj;
            List<${className}DO> list = this.${changeClassName}Service.listByIds(ids);
            result = this.${changeClassName}MapStruct.toDTO(list);
        } else {
            Long id = (Long) idObj;
            ${className}DO ${changeClassName}DO = this.${changeClassName}Service.getById(id);
            result = this.${changeClassName}MapStruct.toDTO(${changeClassName}DO);
        }
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, result);
        return null;
    }
}
