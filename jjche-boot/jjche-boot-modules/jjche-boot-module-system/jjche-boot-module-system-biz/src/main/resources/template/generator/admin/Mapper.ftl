package ${packageService}.mapper;

import ${packageService}.domain.${className}DO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.common.param.PageParam;
import ${packageApi}.vo.${className}VO;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import java.util.List;

/**
* <p>
* ${apiAlias}
* </p>
*
* @author ${author}
* @since ${date}
*/
public interface ${className}Mapper extends MyBaseMapper<${className}DO> {

    /**
    * <p>
    * 分页查询
    * </p>
    *
    * @param page 分页
    * @param wrapper 自定义sql
    * @return 分页VO
    */
    MyPage<${className}VO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);
}