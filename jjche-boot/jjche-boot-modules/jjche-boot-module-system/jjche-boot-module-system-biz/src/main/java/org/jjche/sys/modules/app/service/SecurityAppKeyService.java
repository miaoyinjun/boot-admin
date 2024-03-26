package org.jjche.sys.modules.app.service;

import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.sys.modules.app.constant.AppCacheKey;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.FileUtil;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.modules.app.dto.SecurityAppKeyDTO;
import org.jjche.sys.modules.app.dto.SecurityAppKeyQueryCriteriaDTO;
import org.jjche.sys.modules.app.vo.SecurityAppKeyVO;
import org.jjche.sys.modules.app.domain.SecurityAppKeyDO;
import org.jjche.sys.modules.app.mapper.SecurityAppKeyMapper;
import org.jjche.sys.modules.app.mapstruct.SecurityAppKeyMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用密钥 服务实现类
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Service
@RequiredArgsConstructor
public class SecurityAppKeyService extends MyServiceImpl<SecurityAppKeyMapper, SecurityAppKeyDO> {

    private final SecurityAppKeyMapStruct securityAppKeyMapStruct;
    private final RedisService redisService;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param query 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(SecurityAppKeyQueryCriteriaDTO query) {
        return MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
    }

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     * @return id
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SecurityAppKeyDTO dto) {
        //唯一索引验证
        String name = dto.getName();
        LambdaQueryWrapper<SecurityAppKeyDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SecurityAppKeyDO::getName, name);
        SecurityAppKeyDO dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, name + "已存在");
        //唯一索引验证
        String appId = dto.getAppId();
        queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SecurityAppKeyDO::getAppId, appId);
        dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, appId + "已存在");
        SecurityAppKeyDO securityAppKeyDO = this.securityAppKeyMapStruct.toDO(dto);
        Assert.isTrue(this.save(securityAppKeyDO), "保存失败");
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param ids 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        List<SecurityAppKeyDO> list = this.listByIds(ids);
        Set<String> appIdList = list.stream().map(SecurityAppKeyDO::getAppId).collect(Collectors.toSet());
        //删除缓存
        redisService.delByKeys(AppCacheKey.SECURITY_APP_ID, appIdList);

        Assert.isTrue(this.removeBatchByIdsWithFill(new SecurityAppKeyDO(), ids) == ids.size(), "删除失败，记录不存在");
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param dto 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheInvalidate(name = AppCacheKey.SECURITY_APP_ID, key = "#dto.appId")
    public SecurityAppKeyDO update(SecurityAppKeyDTO dto) {
        SecurityAppKeyDO securityAppKeyDO = this.getById(dto.getId());
        Assert.notNull(securityAppKeyDO, "记录不存在");
        //唯一索引验证
        String name = dto.getName();
        LambdaQueryWrapper<SecurityAppKeyDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SecurityAppKeyDO::getName, name);
        queryWrapper.ne(SecurityAppKeyDO::getId, dto.getId());
        SecurityAppKeyDO dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, name + "已存在");
        //唯一索引验证
        String appId = dto.getAppId();
        queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SecurityAppKeyDO::getAppId, appId);
        queryWrapper.ne(SecurityAppKeyDO::getId, dto.getId());
        dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, appId + "已存在");

        securityAppKeyDO = this.securityAppKeyMapStruct.toDO(dto);
        Assert.isTrue(this.updateById(securityAppKeyDO), "修改失败，记录不存在");
        return securityAppKeyDO;
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return SecurityAppKeyVO 对象
     */
    public SecurityAppKeyVO getVoById(Long id) {
        SecurityAppKeyDO securityAppKeyDO = this.getById(id);
        Assert.notNull(securityAppKeyDO, "记录不存在");
        return this.securityAppKeyMapStruct.toVO(securityAppKeyDO);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param query 条件
     * @param page  分页
     * @return SecurityAppKeyVO 分页
     */
    public MyPage<SecurityAppKeyVO> page(PageParam page, SecurityAppKeyQueryCriteriaDTO query) {
        LambdaQueryWrapper<SecurityAppKeyDO> queryWrapper = queryWrapper(query);
        return this.baseMapper.pageQuery(page, queryWrapper);
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param query 条件
     * @return SecurityAppKeyVO 列表对象
     */
    public List<SecurityAppKeyVO> list(SecurityAppKeyQueryCriteriaDTO query) {
        LambdaQueryWrapper<SecurityAppKeyDO> queryWrapper = queryWrapper(query);
        List<SecurityAppKeyDO> list = this.list(queryWrapper);
        return securityAppKeyMapStruct.toVO(list);
    }

    /**
     * <p>
     * 导出数据
     * </p>
     *
     * @param query 条件
     */
    public void download(SecurityAppKeyQueryCriteriaDTO query) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SecurityAppKeyVO> all = this.list(query);
        for (SecurityAppKeyVO securityAppKey : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", securityAppKey.getName());
            map.put("描述", securityAppKey.getComment());
            map.put("应用id", securityAppKey.getAppId());
            map.put("应用密钥", securityAppKey.getAppSecret());
            map.put("加密密钥", securityAppKey.getEncKey());
            map.put("状态：1启用、0禁用", securityAppKey.getEnabled());
            map.put("地址", securityAppKey.getUrls());
            map.put("白名单", securityAppKey.getWhiteIp());
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        } catch (IOException e) {
            throw new IllegalArgumentException("文件下载失败");
        }
    }

    /**
     * <p>
     * 根据应用id获取密钥
     * </p>
     *
     * @param appId 应用id
     * @return /
     */
    @Cached(name = AppCacheKey.SECURITY_APP_ID, key = "#appId")
    public SecurityAppKeyBasicVO getKeyByAppId(String appId) {
        LambdaQueryWrapper<SecurityAppKeyDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SecurityAppKeyDO::getAppId, appId);
        queryWrapper.eq(SecurityAppKeyDO::getEnabled, true);
        return this.securityAppKeyMapStruct.toBasicVO(this.getOne(queryWrapper));
    }

}
