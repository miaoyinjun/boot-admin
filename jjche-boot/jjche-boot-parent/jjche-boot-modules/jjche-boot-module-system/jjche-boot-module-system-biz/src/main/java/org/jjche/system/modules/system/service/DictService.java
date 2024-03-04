package org.jjche.system.modules.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.DictDTO;
import org.jjche.system.modules.system.api.dto.DictQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.DictDO;
import org.jjche.system.modules.system.mapper.DictMapper;
import org.jjche.system.modules.system.mapstruct.DictMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>DictService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Service
@RequiredArgsConstructor
public class DictService extends MyServiceImpl<DictMapper, DictDO> {

    private final DictMapStruct dictMapper;
    private final RedisService redisService;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(DictQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(name LIKE {0} OR description LIKE {0})", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(DictQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage<DictDO> myPage = this.page(pageable, queryWrapper);
        List<DictDTO> list = dictMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    public List<DictDTO> queryAll(DictQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return dictMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDO resources) {
        this.save(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDO resources) {
        // 清理缓存
        delCaches(resources);
        DictDO dict = this.getById(resources.getId());
        ValidationUtil.isNull(dict.getId(), "DictDO", "id", resources.getId());
        resources.setId(dict.getId());
        this.updateById(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        // 清理缓存
        List<DictDO> dicts = this.listByIds(ids);
        for (DictDO dict : dicts) {
            delCaches(dict);
        }
        this.removeByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param dictDtos 待导出的数据
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<DictDTO> dictDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDTO dictDTO : dictDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("字典名称", dictDTO.getName());
            map.put("字典描述", dictDTO.getDescription());
            map.put("字典标签", null);
            map.put("字典值", null);
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据字典名称获取
     *
     * @param name 字典名称
     * @return /
     */
    public DictDO getByName(String name) {
        LambdaQueryWrapper<DictDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictDO::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>delCaches.</p>
     *
     * @param dict a {@link DictDO} object.
     */
    public void delCaches(DictDO dict) {
        redisService.delete(CacheKey.DIC_NAME + dict.getName());
    }
}
