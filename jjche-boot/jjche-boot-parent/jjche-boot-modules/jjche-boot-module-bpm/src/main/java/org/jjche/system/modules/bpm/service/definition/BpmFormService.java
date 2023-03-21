package org.jjche.system.modules.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.bpm.rest.admin.definition.dto.form.BpmFormDTO;
import org.jjche.system.modules.bpm.rest.admin.definition.dto.form.BpmFormQueryCriteriaDTO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormRespVO;
import org.jjche.system.modules.bpm.convert.definition.BpmFormConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmFormDO;
import org.jjche.system.modules.bpm.enums.definition.BpmModelFormTypeEnum;
import org.jjche.system.modules.bpm.mapper.BpmFormMapper;
import org.jjche.system.modules.bpm.service.definition.dto.BpmFormFieldRespDTO;
import org.jjche.system.modules.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
/**
 * 动态表单 Service 实现类
 *
 * @author 风里雾里
 */
@Service
@Validated
public class BpmFormService extends MyServiceImpl<BpmFormMapper, BpmFormDO>{
//
//    @Resource
//    @Lazy
//    private BpmFormConvert bpmFormConvert;

    /**
     * 创建动态表单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    public Long createForm(BpmFormDTO createReqVO) {
        // 插入
        BpmFormDO form = BpmFormConvert.INSTANCE.convert(createReqVO);
        this.baseMapper.insert(form);
        // 返回
        return form.getId();
    }

    /**
     * 更新动态表单
     *
     * @param updateReqVO 更新信息
     */
    public void updateForm(BpmFormDTO updateReqVO) {
        // 更新
        BpmFormDO updateObj = BpmFormConvert.INSTANCE.convert(updateReqVO);
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
    public BpmFormDO getForm(Long id) {
        return this.baseMapper.selectById(id);
    }

    /**
     * 获得动态表单列表
     *
     * @return 动态表单列表
     */
    public List<BpmFormDO> getFormList() {
        return this.baseMapper.selectList(null);
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
        return BpmFormConvert.INSTANCE.convertPage(this.baseMapper.selectPage(page, queryWrapper));
    }

    /**
     * 校验流程表单已配置
     *
     * @param configStr  configStr 字段
     * @return 流程表单
     */
    public BpmFormDO checkFormConfig(String configStr) {
        BpmModelMetaInfoRespDTO metaInfo = JSONUtil.toBean(configStr, BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw new BusinessException("部署流程失败，原因：流程表单未配置，请点击【修改流程】按钮进行配置");
        }
        // 校验表单存在
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            BpmFormDO form = getForm(metaInfo.getFormId());
            if (form == null) {
                throw new BusinessException("动态表单不存在");
            }
            return form;
        }
        return null;
    }

    /**
     * 校验 Field，避免 field 重复
     *
     * @param fields field 数组
     */
    private void checkFields(List<String> fields) {
        if (true) { // TODO 芋艿：兼容 Vue3 工作流：因为采用了新的表单设计器，所以暂时不校验
            return;
        }
        Map<String, String> fieldMap = new HashMap<>(); // key 是 vModel，value 是 label
        for (String field : fields) {
            BpmFormFieldRespDTO fieldDTO = JSONUtil.toBean(field, BpmFormFieldRespDTO.class);
            Assert.notNull(fieldDTO);
            String oldLabel = fieldMap.put(fieldDTO.getVModel(), fieldDTO.getLabel());
            // 如果不存在，则直接返回
            if (oldLabel == null) {
                continue;
            }
            // 如果存在，则报错
            String msg = StrUtil.format("表单项({}) 和 ({}) 使用了相同的字段名({})", oldLabel, fieldDTO.getLabel(), fieldDTO.getVModel());
            throw new BusinessException(msg);
        }
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
