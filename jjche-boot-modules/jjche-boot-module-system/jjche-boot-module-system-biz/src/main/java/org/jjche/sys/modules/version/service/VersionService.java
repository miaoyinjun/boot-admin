package org.jjche.sys.modules.version.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.version.domain.VersionDO;
import org.jjche.sys.modules.version.dto.VersionDTO;
import org.jjche.sys.modules.version.dto.VersionQueryCriteriaDTO;
import org.jjche.sys.modules.version.mapper.VersionMapper;
import org.jjche.sys.modules.version.mapstruct.VersionMapStruct;
import org.jjche.sys.modules.version.vo.VersionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 版本 服务实现类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Service
@RequiredArgsConstructor
public class VersionService extends MyServiceImpl<VersionMapper, VersionDO> {
    private final VersionMapStruct versionMapStruct;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(VersionDTO dto) {
        LambdaQueryWrapper<VersionDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(VersionDO::getName, dto.getName());
        AssertUtil.isNull(this.getOne(queryWrapper), SysErrorCodeEnum.VERSION_NAME_ALREADY_ERROR);
        VersionDO versionDO = this.versionMapStruct.toDO(dto);
        versionDO.setIsActivated(false);
        this.save(versionDO);
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param dto 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(VersionDTO dto) {
        VersionDO versionDO = this.getById(dto.getId());
        AssertUtil.notNull(versionDO, SysErrorCodeEnum.RECORD_NOT_FOUND);
        String name = dto.getName();

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        VersionDO versionDoDb = this.getOne(queryWrapper);
        Boolean isExist = versionDoDb != null && !versionDoDb.getId().equals(dto.getId());
        AssertUtil.isFalse(isExist, SysErrorCodeEnum.VERSION_NAME_ALREADY_ERROR);
        versionDO.setName(name);
        versionDO.setRemark(dto.getRemark());
        this.updateById(versionDO);
    }

    /**
     * <p>
     * 编辑激活
     * </p>
     *
     * @param id 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateActivated(Long id) {
        //重置记录激活状态为否
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_activated", true);
        VersionDO versionDO = this.getOne(queryWrapper);
        versionDO.setIsActivated(false);
        this.updateById(versionDO);
        //设置新的激活记录
        versionDO = this.getById(id);
        AssertUtil.notNull(versionDO, SysErrorCodeEnum.RECORD_NOT_FOUND);
        versionDO.setIsActivated(true);
        this.updateById(versionDO);
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return VersionVO 对象
     */
    public VersionVO getVoById(Long id) {
        VersionDO versionDO = this.getById(id);
        AssertUtil.notNull(versionDO, SysErrorCodeEnum.RECORD_NOT_FOUND);
        return this.versionMapStruct.toVO(versionDO);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param criteria 条件
     * @param page     page
     * @return VersionVO 分页
     */
    public MyPage<VersionVO> queryAll(VersionQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper<VersionDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        queryWrapper.orderByDesc(CollUtil.newArrayList(VersionDO::getIsActivated, VersionDO::getId));
        return this.baseMapper.pageQuery(page, queryWrapper);
    }

    /**
     * <p>
     * 获取最新版本号
     * </p>
     *
     * @return 最新版本号
     */
    public String versionLatest() {
        String version = "error";
        try {
            LambdaQueryWrapper<VersionDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(VersionDO::getIsActivated, true);
            VersionDO versionDO = this.getOne(queryWrapper);
            version = versionDO.getName();
        } catch (Exception e) {
            StaticLog.error(ThrowableUtil.getStackTrace(e));
        }
        return version;
    }
}
