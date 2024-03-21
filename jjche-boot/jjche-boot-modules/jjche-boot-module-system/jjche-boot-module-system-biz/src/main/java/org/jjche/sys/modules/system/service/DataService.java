package org.jjche.sys.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.RoleSmallDTO;
import org.jjche.common.dto.UserVO;
import org.jjche.sys.modules.system.api.enums.DataScopeEnum;
import org.jjche.common.vo.DataScopeVO;
import org.jjche.sys.modules.system.domain.DeptDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>数据权限服务实现</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2020-05-07
 */
@Service
@RequiredArgsConstructor
public class DataService {

    private final RoleService roleService;
    private final DeptService deptService;

    /**
     * <p>
     * 用户角色改变时需清理缓存
     * </p>
     *
     * @param user 用户
     * @return /
     */
    public DataScopeVO getDataScope(UserVO user) {
        // 查询用户角色
        List<RoleSmallDTO> roleSet = roleService.findByUsersId(user.getId());
        // 获取对应的部门ID
        DataScopeVO dataScopeVO = new DataScopeVO();
        for (RoleSmallDTO role : roleSet) {

            DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
            //权限：全部，跳出
            if (dataScopeEnum == DataScopeEnum.DATA_SCOPE_ALL) {
                dataScopeVO.setAll(true);
                dataScopeVO.setDeptIds(CollUtil.newHashSet());
                break;
            }
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case DATA_SCOPE_DEPT_AND_CHILD:
                    dataScopeVO.getDeptIds().add(user.getDept().getId());
                    dataScopeVO.getDeptIds().addAll(deptService.getDeptChildrenIds(user.getDept().getId()));
                    break;
                case DATA_SCOPE_DEPT:
                    dataScopeVO.getDeptIds().addAll(CollUtil.newArrayList(user.getDept().getId()));
                    break;
                case DATA_SCOPE_SELF:
                    dataScopeVO.setSelf(true);
                    dataScopeVO.setUserId(user.getId());
                    dataScopeVO.setUserName(user.getUsername());
                    break;
                case DATA_SCOPE_CUSTOM:
                    dataScopeVO.getDeptIds().addAll(getCustomize(role));
                    break;
                default:
            }
        }
        return dataScopeVO;
    }

    /**
     * 获取自定义的数据权限
     *
     * @param role 角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(RoleSmallDTO role) {
        Set<Long> deptIds = CollUtil.newHashSet();
        List<DeptDO> depts = deptService.findByRoleId(role.getId());
        for (DeptDO dept : depts) {
            deptIds.add(dept.getId());
            List<DeptDO> deptChildren = deptService.findByPid(dept.getId());
            if (deptChildren != null && deptChildren.size() != 0) {
                deptIds.addAll(deptService.getDeptChildren(deptChildren));
            }
        }
        return deptIds;
    }
}
