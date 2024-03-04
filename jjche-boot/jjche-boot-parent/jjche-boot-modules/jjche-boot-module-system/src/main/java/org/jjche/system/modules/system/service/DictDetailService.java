package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.system.api.dict.DictApi;
import org.jjche.common.util.StrUtil;
import org.jjche.common.util.ValidationUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.DictDetailDTO;
import org.jjche.system.modules.system.api.dto.DictDetailQueryCriteriaDTO;
import org.jjche.system.modules.system.api.dto.DictSmallDto;
import org.jjche.system.modules.system.conf.DictRunner;
import org.jjche.system.modules.system.domain.DictDO;
import org.jjche.system.modules.system.domain.DictDetailDO;
import org.jjche.system.modules.system.mapper.DictDetailMapper;
import org.jjche.system.modules.system.mapstruct.DictDetailMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>DictDetailService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Service
@RequiredArgsConstructor
public class DictDetailService extends MyServiceImpl<DictDetailMapper, DictDetailDO> implements DictApi {

    private final DictService dictService;
    private final DictDetailMapStruct dictDetailMapper;
    @CreateCache(name = CacheKey.DIC_NAME)
    private Cache<String, List<DictDetailDTO>> dicDetailCache;

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(DictDetailQueryCriteriaDTO criteria, PageParam pageable) {
        String dictName = criteria.getDictName();
        //非管理页面请求
        if (StrUtil.isNotBlank(dictName)) {
            DictDO dictDO = dictService.getByName(dictName);
            if (dictDO != null) {
                criteria.setDictId(dictDO.getId());
            } else {
                criteria.setDictId(0L);
            }
        }
        LambdaQueryWrapper<DictDetailDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        queryWrapper.orderByAsc(DictDetailDO::getDictSort);
        MyPage<DictDetailDO> myPage = this.page(pageable, queryWrapper);
        List<DictDetailDTO> list = dictDetailMapper.toVO(myPage.getRecords());
        Long dictId = criteria.getDictId();
        if (dictId != null) {
            DictSmallDto dto = new DictSmallDto();
            dto.setId(dictId);
            for (DictDetailDTO detailDTO : list) {
                detailDTO.setDict(dto);
            }
        }
        //如果为空，查询枚举
        if (CollUtil.isEmpty(list)) {
            list = MapUtil.get(DictRunner.DICT_ENUMS, dictName, List.class);
        }
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDetailDO resources) {
        this.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetailDO resources) {
        Long id = resources.getId();
        DictDetailDO dictDetail = this.getById(id);
        ValidationUtil.isNull(dictDetail, "DictDetailDO", "id", resources.getId());
        resources.setId(id);
        this.updateById(resources);
        // 清理缓存
        delCaches(resources);
    }

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */
    @Cached(name = CacheKey.DIC_NAME, key = "#name")
    public List<DictDetailDTO> getDictByName(String name) {
        DictDO dictDO = dictService.getByName(name);
        Assert.notNull(dictDO, "未找到字典");
        Long dictId = dictDO.getId();
        LambdaQueryWrapper<DictDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictDetailDO::getDictId, dictId);
        return dictDetailMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 删除
     *
     * @param id /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DictDetailDO dictDetail = this.getById(id);
        // 清理缓存
        delCaches(dictDetail);
        this.removeById(id);
    }

    /**
     * <p>delCaches.</p>
     *
     * @param dictDetail a {@link DictDetailDO} object.
     */
    public void delCaches(DictDetailDO dictDetail) {
        DictDO dict = dictService.getById(dictDetail.getDictId());
        dicDetailCache.remove(dict.getName());
    }

    /**
     * <p>
     * 查询字典项
     * </p>
     *
     * @param dictId 字典类型
     * @param values 字典项
     * @return /
     */
    private List<DictDetailDO> listByDictIdAndValues(Long dictId, Collection<String> values) {
        return this.list(new LambdaQueryWrapper<DictDetailDO>().eq(DictDetailDO::getDictId, dictId)
                .in(DictDetailDO::getValue, values));
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        DictDO dictDO = this.dictService.getByName(dictType);
        Map<String, DictDetailDO> dictDataMap = MapUtil.newHashMap();
        List<DictDetailDO> dictDetails = this.listByDictIdAndValues(dictDO.getId(), values);
        dictDataMap = CollUtil.toMap(dictDetails, dictDataMap, DictDetailDO::getValue);
        // 校验
        Map<String, DictDetailDO> finalDictDataMap = dictDataMap;
        values.forEach(value -> {
            DictDetailDO dictData = finalDictDataMap.get(value);
            Assert.notNull(dictData, "当前字典数据不存在");
        });
    }
}
