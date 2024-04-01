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
import ${packageApi}.dto.${className}QueryCriteriaDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;

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
    * 获取列表查询语句
    * </p>
    *
    * @param query 条件
    * @return sql
    */
    default LambdaQueryWrapper queryWrapper(${className}QueryCriteriaDTO query) {
        return MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
    }

    /**
    * <p>
    * 查询数据分页
    * </p>
    * @param query 条件
    * @param page 分页
    * @return ${className}VO 分页
    */
    default MyPage<${className}VO> page(PageParam page, ${className}QueryCriteriaDTO query) {
        LambdaQueryWrapper<${className}DO> queryWrapper = queryWrapper(query);
        return this.pageQuery(page, queryWrapper);
    }

    /**
    * <p>
    * 查询所有数据不分页
    * </p>
    * @param query 条件
    * @return ${className}VO 列表对象
    */
    default List<${className}DO> list(${className}QueryCriteriaDTO query){
      LambdaQueryWrapper<${className}DO> queryWrapper = queryWrapper(query);
      return this.selectList(queryWrapper);
    }

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
