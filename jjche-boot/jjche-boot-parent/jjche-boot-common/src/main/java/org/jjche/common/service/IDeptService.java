package org.jjche.common.service;

import org.jjche.common.dto.DeptSmallDto;

import java.util.List;
import java.util.Set;

/**
 * <p>系统用户部门</p>
 *
 * @author miaoyj
 */
public interface IDeptService {

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    List<DeptSmallDto> listByIds(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    DeptSmallDto getSmallById(Long id);
}
