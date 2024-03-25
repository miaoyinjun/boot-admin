package org.jjche.bpm.modules.form.mapper;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.form.domain.BpmFormDO;
import org.jjche.bpm.modules.form.dto.BpmFormQueryCriteriaDTO;
import org.jjche.common.param.PageParam;
import org.jjche.common.param.MyPage;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.MyBaseMapper;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface BpmFormMapper extends MyBaseMapper<BpmFormDO> {

    default MyPage<BpmFormDO> selectPage(PageParam page, BpmFormQueryCriteriaDTO reqVO) {
        LambdaQueryWrapper<BpmFormDO> wrapper = Wrappers.lambdaQuery();
        String name = reqVO.getName();
        if (StrUtil.isNotBlank(name)) {
            wrapper.eq(BpmFormDO::getName, name);
        }
        return this.selectPage(page, wrapper);
    }

}
