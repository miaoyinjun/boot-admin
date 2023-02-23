package org.jjche.bpm.modules.bpm.dal.mysql.definition;


import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.bpm.controller.admin.definition.vo.form.BpmFormPageReqVO;
import org.jjche.bpm.modules.bpm.dal.dataobject.definition.BpmFormDO;
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

    default MyPage<BpmFormDO> selectPage(BpmFormPageReqVO reqVO) {
        LambdaQueryWrapper<BpmFormDO> wrapper = Wrappers.lambdaQuery();
        String name = reqVO.getName();
        if(StrUtil.isNotBlank(name)){
            wrapper.eq(BpmFormDO::getName, name);
        }
        return this.selectPage(reqVO, wrapper);
    }

}
