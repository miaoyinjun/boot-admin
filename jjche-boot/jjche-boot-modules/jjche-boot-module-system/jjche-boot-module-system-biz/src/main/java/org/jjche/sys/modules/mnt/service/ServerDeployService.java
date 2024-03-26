package org.jjche.sys.modules.mnt.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.modules.mnt.domain.ServerDeployDO;
import org.jjche.sys.modules.mnt.dto.ServerDeployDTO;
import org.jjche.sys.modules.mnt.dto.ServerDeployQueryCriteriaDTO;
import org.jjche.sys.modules.mnt.mapper.ServerDeployMapper;
import org.jjche.sys.modules.mnt.mapstruct.ServerDeployMapStruct;
import org.jjche.sys.modules.mnt.util.ExecuteShellUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>ServerDeployService class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Service
@RequiredArgsConstructor
public class ServerDeployService extends MyServiceImpl<ServerDeployMapper, ServerDeployDO> {

    private final ServerDeployMapStruct serverDeployMapper;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(ServerDeployQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(name LIKE {0} OR ip LIKE {0} OR account LIKE {0})", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(ServerDeployQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<ServerDeployDTO> list = serverDeployMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<ServerDeployDTO> queryAll(ServerDeployQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public ServerDeployDTO findById(Long id) {
        ServerDeployDO server = this.getById(id);
        ValidationUtil.isNull(server.getId(), "ServerDeployDO", "id", id);
        return serverDeployMapper.toVO(server);
    }

    /**
     * 根据IP查询
     *
     * @param ip /
     * @return /
     */
    public ServerDeployDTO findByIp(String ip) {
        LambdaQueryWrapper<ServerDeployDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServerDeployDO::getIp, ip);
        return serverDeployMapper.toVO(this.getOne(queryWrapper));
    }

    /**
     * 测试登录服务器
     *
     * @param resources /
     * @return /
     */
    public Boolean testConnect(ServerDeployDO resources) {
        ExecuteShellUtil executeShellUtil = null;
        try {
            executeShellUtil = new ExecuteShellUtil(resources.getIp(), resources.getAccount(), resources.getPassword(), resources.getPort());
            return executeShellUtil.execute("ls") == 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (executeShellUtil != null) {
                executeShellUtil.close();
            }
        }
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(ServerDeployDO resources) {
        this.save(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ServerDeployDO resources) {
        this.updateById(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<ServerDeployDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ServerDeployDTO deployDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("服务器名称", deployDto.getName());
            map.put("服务器IP", deployDto.getIp());
            map.put("端口", deployDto.getPort());
            map.put("账号", deployDto.getAccount());
            map.put("创建日期", deployDto.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
