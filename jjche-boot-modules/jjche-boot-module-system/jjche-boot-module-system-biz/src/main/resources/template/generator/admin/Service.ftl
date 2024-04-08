package ${packageService}.service;

import ${packageService}.domain.${className}DO;
import ${packageApi}.dto.${className}DTO;
import ${packageApi}.vo.${className}VO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jjche.mybatis.base.service.IMyService;
import org.jjche.common.param.PageParam;
import javax.servlet.http.HttpServletResponse;
import ${packageApi}.dto.${className}QueryCriteriaDTO;
import org.jjche.common.param.MyPage;
import java.util.*;

/**
* <p>
* ${apiAlias} 服务接口
* </p>
*
* @author ${author}
* @since ${date}
*/
public interface I${className}Service extends IMyService<${className}DO> {

    /**
    * <p>
    * 创建
    * </p>
    * @param dto 创建对象
    * @author ${author}
    * @since ${date}
    */
    void save(${className}DTO dto);

   /**
   * <p>
   * 多选删除
   * </p>
   * @param ids 主键
   * @author ${author}
   * @since ${date}
   */
   void delete(Set<${pkColumnType}> ids);

    /**
    * <p>
    * 编辑
    * </p>
    * @param dto 编辑对象
    * @author ${author}
    * @since ${date}
    */
    void update(${className}DTO dto);

    /**
    * <p>
    * 根据ID查询
    * </p>
    * @param ${pkChangeColName} ID
    * @return ${className}VO 对象
    * @author ${author}
    * @since ${date}
    */
    ${className}VO getVOById(${pkColumnType} ${pkChangeColName});

    /**
    * <p>
    * 查询数据分页
    * </p>
    * @param query 条件
    * @param page 分页
    * @return ${className}VO 分页
    * @author ${author}
    * @since ${date}
    */
    MyPage<${className}VO> pageQuery(PageParam page, ${className}QueryCriteriaDTO query);

    /**
    * <p>
    * 查询所有数据不分页
    * </p>
    * @param query 条件
    * @return ${className}VO 列表对象
    * @author ${author}
    * @since ${date}
    */
    List<${className}VO> listQueryAll(${className}QueryCriteriaDTO query);

    /**
    * <p>
    * 导出数据
    * </p>
    * @param query 条件
    * @author ${author}
    * @since ${date}
    */
    void download(${className}QueryCriteriaDTO query);


}