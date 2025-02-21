package org.jjche.sys.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.DictParam;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.system.conf.DictRunner;
import org.jjche.sys.modules.system.constant.DictCacheKey;
import org.jjche.sys.modules.system.domain.DictDO;
import org.jjche.sys.modules.system.domain.DictDetailDO;
import org.jjche.sys.modules.system.dto.DictDetailDTO;
import org.jjche.sys.modules.system.dto.DictDetailQueryCriteriaDTO;
import org.jjche.sys.modules.system.dto.DictSmallDto;
import org.jjche.sys.modules.system.mapper.DictDetailMapper;
import org.jjche.sys.modules.system.mapstruct.DictDetailMapStruct;
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
public class DictDetailService extends MyServiceImpl<DictDetailMapper, DictDetailDO> {

    private final DictService dictService;
    private final DictDetailMapStruct dictDetailMapper;
    @CreateCache(name = DictCacheKey.DIC_NAME)
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
        AssertUtil.notNull(dictDetail, SysErrorCodeEnum.RECORD_NOT_FOUND);
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
    @Cached(name = DictCacheKey.DIC_NAME, key = "#name")
    public List<DictDetailDTO> getDictByName(String name) {
        DictDO dictDO = dictService.getByName(name);
        AssertUtil.notNull(dictDO, SysErrorCodeEnum.DICT_NOT_FOUND_ERROR);
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

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    public void validDictList(String dictType, Collection<String> values) {
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
            AssertUtil.notNull(dictData, SysErrorCodeEnum.DICT_DETAIL_NOT_FOUND_ERROR);
        });
    }

    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    public DictParam getDictByNameValue(String name, String value) {
        List<DictDetailDTO> list = this.getDictByName(name);
        if (CollUtil.isNotEmpty(list)) {
            DictDetailDTO dictDetailDTO = null;
            for (DictDetailDTO dict : list) {
                if (StrUtil.equals(dict.getValue(), value)) {
                    dictDetailDTO = dict;
                    break;
                }
            }
            if (dictDetailDTO != null) {
                DictParam dictParam = new DictParam();
                dictParam.setValue(dictDetailDTO.getValue());
                dictParam.setLabel(dictDetailDTO.getLabel());
                return dictParam;
            }
        }
        return null;
    }
}
