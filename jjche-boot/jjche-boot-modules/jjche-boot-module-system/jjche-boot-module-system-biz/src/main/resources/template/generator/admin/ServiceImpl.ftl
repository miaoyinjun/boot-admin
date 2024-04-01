package ${packageService}.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import ${packageService}.mapper.${className}Mapper;
import ${packageService}.domain.${className}DO;
import org.jjche.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import ${packageApi}.dto.${className}DTO;
import ${packageApi}.dto.${className}QueryCriteriaDTO;
import ${packageApi}.vo.${className}VO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.jjche.common.param.PageParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.common.param.MyPage;
import ${packageService}.mapstruct.${className}MapStruct;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import cn.hutool.core.lang.Assert;
import java.util.List;
/**
* <p>
* ${apiAlias} 服务实现类
* </p>
*
* @author ${author}
* @since ${date}
*/
@Service
@RequiredArgsConstructor
public class ${className}Service extends MyServiceImpl<${className}Mapper, ${className}DO> {

    private final ${className}MapStruct ${changeClassName}MapStruct;

   /**
   * <p>
   * 创建
   * </p>
   * @param dto 创建对象
   * @return id
   */
    @Transactional(rollbackFor = Exception.class)
    public Long save(${className}DTO dto) {
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>
        //唯一索引验证
        ${column.columnType} ${column.changeColumnName} = dto.get${column.capitalColumnName}();
        LambdaQueryWrapper<${className}DO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(${className}DO::get${column.capitalColumnName}, ${column.changeColumnName});
        ${className}DO dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, ${column.changeColumnName} + "已存在");
            </#if>
        </#list>
    </#if>
        ${className}DO ${changeClassName}DO = this.${changeClassName}MapStruct.toDO(dto);
        Assert.isTrue(this.save(${changeClassName}DO), "保存失败");
        return ${changeClassName}DO.getId();
    }

    /**
    * <p>
    * 多选删除
    * </p>
    * @param ids 主键
    */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<${pkColumnType}> ids){
        Assert.isTrue(this.removeBatchByIdsWithFill(new ${className}DO(), ids) == ids.size(), "删除失败，记录不存在");
    }

    /**
    * <p>
    * 编辑
    * </p>
    * @param dto 编辑对象
    */
    @Transactional(rollbackFor = Exception.class)
    public void update(${className}DTO dto) {
        ${className}DO ${changeClassName}DO = this.getById(dto.getId());
        Assert.notNull(${changeClassName}DO, "记录不存在");
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
        //唯一索引验证
        ${column.columnType} ${column.changeColumnName} = dto.get${column.capitalColumnName}();
        LambdaQueryWrapper<${className}DO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(${className}DO::get${column.capitalColumnName}, ${column.changeColumnName});
        queryWrapper.ne(${className}DO::getId, dto.getId());
        ${className}DO dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, ${column.changeColumnName} + "已存在");
        </#if>
    </#list>
</#if>
        ${changeClassName}DO = this.${changeClassName}MapStruct.toDO(dto);
        Assert.isTrue(this.updateById(${changeClassName}DO), "修改失败，记录不存在");
}
    /**
    * <p>
    * 根据ID查询
    * </p>
    * @param ${pkChangeColName} ID
    * @return ${className}VO 对象
    */
    public ${className}VO getVoById(${pkColumnType} ${pkChangeColName}) {
        ${className}DO ${changeClassName}DO = this.getById(id);
        Assert.notNull(${changeClassName}DO, "记录不存在");
        return this.${changeClassName}MapStruct.toVO(${changeClassName}DO);
    }

    /**
    * <p>
    * 查询数据分页
    * </p>
    * @param query 条件
    * @param page 分页
    * @return ${className}VO 分页
    */
    public MyPage<${className}VO> page(PageParam page, ${className}QueryCriteriaDTO query) {
        return this.baseMapper.page(page, query);
    }

    /**
    * <p>
    * 查询所有数据不分页
    * </p>
    * @param query 条件
    * @return ${className}VO 列表对象
    */
    public List<${className}VO> list(${className}QueryCriteriaDTO query){
        List<${className}DO> list = this.baseMapper.list(query);
        return ${changeClassName}MapStruct.toVO(list);
    }

    /**
    * <p>
    * 导出数据
    * </p>
    * @param query 条件
    */
    public void download(${className}QueryCriteriaDTO query) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<${className}VO> all = this.list(query);
        for (${className}VO ${changeClassName} : all) {
            Map<String,Object> map = new LinkedHashMap<>();
        <#list columns as column>
            <#if column.columnKey != 'PRI'
                && column.columnName != 'gmt_modified'
                && column.columnName != 'is_deleted'
                && column.columnName != 'updated_by'
                && column.columnShow
                >
            <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
            <#else>
            map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
            </#if>
            </#if>
        </#list>
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        }catch (IOException e){
            throw new IllegalArgumentException("文件下载失败");
        }
    }
}
