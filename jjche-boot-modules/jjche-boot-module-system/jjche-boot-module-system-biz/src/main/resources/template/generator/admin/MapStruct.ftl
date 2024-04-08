package ${packageService}.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import ${packageService}.domain.${className}DO;
import ${packageApi}.vo.${className}VO;
import ${packageApi}.dto.${className}DTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* <p>
* ${apiAlias} 转换类
* </p>
*
* @author ${author}
* @since ${date}
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}MapStruct extends BaseMapStruct<${className}DO, ${className}DTO, ${className}VO> {
}