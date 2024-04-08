package org.jjche.sys.modules.mnt.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.modules.mnt.domain.DeployHistoryDO;
import org.jjche.sys.modules.mnt.dto.DeployHistoryDTO;
import org.jjche.sys.modules.mnt.dto.DeployHistoryQueryCriteriaDTO;
import org.jjche.sys.modules.mnt.mapper.DeployHistoryMapper;
import org.jjche.sys.modules.mnt.mapstruct.DeployHistoryMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>DeployHistoryServiceImpl class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Service
@RequiredArgsConstructor
public class DeployHistoryService extends MyServiceImpl<DeployHistoryMapper, DeployHistoryDO> {

    private final DeployHistoryMapStruct deployHistoryMapper;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(DeployHistoryQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(appName LIKE {0} OR ip LIKE {0} OR deployUser LIKE {0})", "%" + blurry + "%");
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
    public MyPage queryAll(DeployHistoryQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<DeployHistoryDTO> list = deployHistoryMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<DeployHistoryDTO> queryAll(DeployHistoryQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return deployHistoryMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public DeployHistoryDTO findById(String id) {
        return deployHistoryMapper.toVO(this.getById(id));
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DeployHistoryDO resources) {
        this.save(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<String> ids) {
        this.removeByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<DeployHistoryDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeployHistoryDTO deployHistoryDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部署编号", deployHistoryDto.getDeployId());
            map.put("应用名称", deployHistoryDto.getAppName());
            map.put("部署IP", deployHistoryDto.getIp());
            map.put("部署时间", deployHistoryDto.getGmtCreate());
            map.put("部署人员", deployHistoryDto.getDeployUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
