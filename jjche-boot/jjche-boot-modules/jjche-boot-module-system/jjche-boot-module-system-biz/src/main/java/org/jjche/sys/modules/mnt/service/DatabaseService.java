package org.jjche.sys.modules.mnt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.modules.mnt.domain.DatabaseDO;
import org.jjche.sys.modules.mnt.api.dto.DatabaseDTO;
import org.jjche.sys.modules.mnt.api.dto.DatabaseQueryCriteriaDTO;
import org.jjche.sys.modules.mnt.mapper.DatabaseMapper;
import org.jjche.sys.modules.mnt.mapstruct.DatabaseMapStruct;
import org.jjche.sys.modules.mnt.util.SqlUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>DatabaseService class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Service
@RequiredArgsConstructor
public class DatabaseService extends MyServiceImpl<DatabaseMapper, DatabaseDO> {
    private final DatabaseMapStruct databaseMapper;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(DatabaseQueryCriteriaDTO criteria) {
        return MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(DatabaseQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<DatabaseDTO> list = databaseMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<DatabaseDTO> queryAll(DatabaseQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return databaseMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public DatabaseDTO findById(String id) {
        DatabaseDO database = this.getById(id);
        ValidationUtil.isNull(database.getId(), "DatabaseDO", "id", id);
        return databaseMapper.toVO(database);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DatabaseDO resources) {
        this.save(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DatabaseDO resources) {
        this.updateById(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<String> ids) {
        this.removeByIds(ids);
    }

    /**
     * 测试连接数据库
     *
     * @param resources /
     * @return /
     */
    public boolean testConnection(DatabaseDO resources) {
        try {
            return SqlUtils.testConnection(resources.getJdbcUrl(), resources.getUserName(), resources.getPwd());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<DatabaseDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DatabaseDTO databaseDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("数据库名称", databaseDto.getName());
            map.put("数据库连接地址", databaseDto.getJdbcUrl());
            map.put("用户名", databaseDto.getUserName());
            map.put("创建日期", databaseDto.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
