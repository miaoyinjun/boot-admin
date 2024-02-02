package org.jjche.bpm.modules.form.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jjche.bpm.modules.form.domain.BpmFormDO;
import org.jjche.bpm.modules.form.api.dto.BpmFormDTO;
import org.jjche.bpm.modules.form.api.dto.BpmFormQueryCriteriaDTO;
import org.jjche.bpm.modules.form.api.vo.BpmFormDetailRespVO;
import org.jjche.bpm.modules.form.api.vo.BpmFormRespVO;
import org.jjche.bpm.modules.form.api.vo.BpmFormSimpleRespVO;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.bpm.modules.form.mapstruct.BpmFormConvert;
import org.jjche.bpm.modules.form.mapper.BpmFormMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * 动态表单 Service 实现类
 *
 * @author 风里雾里
 */
@Service
public class BpmFormService extends MyServiceImpl<BpmFormMapper, BpmFormDO>{

    @Resource
    private BpmFormConvert bpmFormConvert;

    /**
     * 创建动态表单
     *
     * @param createReq 创建信息
     * @return 编号
     */
    public Long createForm(BpmFormDTO createReq) {
        // 插入
        BpmFormDO form = bpmFormConvert.toDO(createReq);
        this.baseMapper.insert(form);
        // 返回
        return form.getId();
    }

    /**
     * 更新动态表单
     *
     * @param updateReq 更新信息
     */
    public void updateForm(BpmFormDTO updateReq) {
        // 更新
        BpmFormDO updateObj = bpmFormConvert.toDO(updateReq);
        this.updateById(updateObj);
    }

    /**
     * 删除动态表单
     *
     * @param ids 编号
     */
    public void deleteForm(List<Long> ids) {
        this.removeByIds(ids);
    }

    private void validateFormExists(Long id) {
        if (this.baseMapper.selectById(id) == null) {
            throw new BusinessException("动态表单不存在");
        }
    }

    /**
     * 获得动态表单
     *
     * @param id 编号
     * @return 动态表单
     */
    public BpmFormDetailRespVO getForm(Long id) {
        return bpmFormConvert.toDetailVO(this.baseMapper.selectById(id));
    }

    /**
     * 获得动态表单列表
     *
     * @return 动态表单列表
     */
    public List<BpmFormSimpleRespVO> getFormList() {
        return bpmFormConvert.toSimpleVO(this.list());
    }

    /**
     * 获得动态表单列表
     *
     * @param ids 编号
     * @return 动态表单列表
     */
    public List<BpmFormDO> getFormList(Collection<Long> ids) {
        return this.baseMapper.selectBatchIds(ids);
    }

    /**
     * 获得动态表单分页
     *
     * @param pageReqVO 分页查询
     * @return 动态表单分页
     */
    public MyPage<BpmFormRespVO> getFormPage(PageParam page,
                                             BpmFormQueryCriteriaDTO pageReqVO) {
        LambdaQueryWrapper<BpmFormDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(pageReqVO, SortEnum.ID_DESC);;
        return bpmFormConvert.toVO(this.baseMapper.selectPage(page, queryWrapper));
    }

    /**
     * 获得动态表单 Map
     *
     * @param ids 编号
     * @return 动态表单 Map
     */
    public Map<Long, BpmFormDO> getFormMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<Long, BpmFormDO> map = MapUtil.newHashMap();
        map = CollUtil.toMap(this.getFormList(ids), map, BpmFormDO::getId);
        return map;
    }
}
