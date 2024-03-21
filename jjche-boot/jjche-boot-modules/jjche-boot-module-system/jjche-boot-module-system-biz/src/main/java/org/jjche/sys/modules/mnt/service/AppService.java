package org.jjche.sys.modules.mnt.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.UserVO;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.modules.mnt.domain.AppDO;
import org.jjche.sys.modules.mnt.api.dto.AppDTO;
import org.jjche.sys.modules.mnt.api.dto.AppQueryCriteriaDTO;
import org.jjche.sys.modules.mnt.mapper.AppMapper;
import org.jjche.sys.modules.mnt.mapstruct.AppMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>AppService class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Service
@RequiredArgsConstructor
public class AppService extends MyServiceImpl<AppMapper, AppDO> {

    private final AppMapStruct appMapStruct;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(AppQueryCriteriaDTO criteria) {
        return MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(AppQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<UserVO> list = appMapStruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<AppDTO> queryAll(AppQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return appMapStruct.toVO(this.list(queryWrapper));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public AppDTO findById(Long id) {
        AppDO app = this.getById(id);
        ValidationUtil.isNull(app.getId(), "AppDO", "id", id);
        return appMapStruct.toVO(app);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(AppDO resources) {
        verification(resources);
        this.save(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AppDO resources) {
        verification(resources);
        this.updateById(resources);
    }

    private void verification(AppDO resources) {
        String opt = "/opt";
        String home = "/home";
        Boolean isUploadOptOrHomePath = !(resources.getUploadPath().startsWith(opt) || resources.getUploadPath().startsWith(home));
        Assert.isFalse(isUploadOptOrHomePath, "文件只能上传在opt目录或者home目录");

        Boolean isDeployOptOrHomePath = !(resources.getDeployPath().startsWith(opt) || resources.getDeployPath().startsWith(home));
        Assert.isFalse(isDeployOptOrHomePath, "文件只能部署在opt目录或者home目录");

        Boolean isBackupOptOrHomePath = !(resources.getBackupPath().startsWith(opt) || resources.getBackupPath().startsWith(home));
        Assert.isFalse(isBackupOptOrHomePath, "文件只能备份在opt目录或者home目录");
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
    public void download(List<AppDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppDTO appDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("应用名称", appDto.getName());
            map.put("端口", appDto.getPort());
            map.put("上传目录", appDto.getUploadPath());
            map.put("部署目录", appDto.getDeployPath());
            map.put("备份目录", appDto.getBackupPath());
            map.put("启动脚本", appDto.getStartScript());
            map.put("部署脚本", appDto.getDeployScript());
            map.put("创建日期", appDto.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
