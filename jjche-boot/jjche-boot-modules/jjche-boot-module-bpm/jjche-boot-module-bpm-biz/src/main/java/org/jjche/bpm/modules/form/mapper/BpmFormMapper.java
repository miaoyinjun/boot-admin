package org.jjche.bpm.modules.form.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.form.domain.BpmFormDO;
import org.jjche.mybatis.base.MyBaseMapper;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface BpmFormMapper extends MyBaseMapper<BpmFormDO> {

}
