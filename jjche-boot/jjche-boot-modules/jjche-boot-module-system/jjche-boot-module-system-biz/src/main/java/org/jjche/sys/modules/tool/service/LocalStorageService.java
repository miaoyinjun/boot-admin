package org.jjche.sys.modules.tool.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.FileType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.core.fileconf.FileProperties;
import org.jjche.core.util.FileUtil;
import org.jjche.filter.enc.field.enums.EncryptField;
import org.jjche.filter.enc.field.enums.EncryptMethod;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.modules.tool.api.dto.LocalStorageDTO;
import org.jjche.sys.modules.tool.api.dto.LocalStorageQueryCriteriaDTO;
import org.jjche.sys.modules.tool.api.vo.LocalStorageBaseVO;
import org.jjche.sys.modules.tool.domain.LocalStorageDO;
import org.jjche.sys.modules.tool.mapper.LocalStorageMapper;
import org.jjche.sys.modules.tool.mapstruct.LocalStorageBaseMapStruct;
import org.jjche.sys.modules.tool.mapstruct.LocalStorageMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>LocalStorageService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Service
@RequiredArgsConstructor
public class LocalStorageService extends MyServiceImpl<LocalStorageMapper, LocalStorageDO> {

    private final LocalStorageMapStruct localStorageMapper;
    private final LocalStorageBaseMapStruct localStorageBaseMapStruct;
    private final FileProperties properties;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(LocalStorageQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(name LIKE {0} OR suffix LIKE {0} OR type LIKE {0})", "%" + blurry + "%");
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
    public MyPage<LocalStorageDTO> pageQuery(LocalStorageQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<LocalStorageDTO> list = localStorageMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<LocalStorageDTO> queryAll(LocalStorageQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return localStorageMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 上传
     *
     * @param name           文件名称
     * @param saveDB          是否保存到数据库中
     * @param multipartFiles an array of {@link MultipartFile} objects.
     * @return a {@link LocalStorageDO} object.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<LocalStorageBaseVO> create(String name, Boolean saveDB, MultipartFile[] multipartFiles) {
        List<LocalStorageDO> list = new ArrayList<>(multipartFiles.length);
        for (MultipartFile multipartFile : multipartFiles) {
            FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
            String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
            FileType type = FileUtil.getFileType(suffix);
            File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type.getValue() + File.separator);
            Assert.notNull(file, "上传失败");
            try {
                name = org.jjche.common.util.StrUtil.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
                LocalStorageDO localStorage = new LocalStorageDO(
                        file.getName(),
                        name,
                        suffix,
                        file.getPath(),
                        type,
                        FileUtil.getSize(multipartFile.getSize())
                );
                list.add(localStorage);
            } catch (Exception e) {
                FileUtil.del(file);
                throw e;
            }
        }
        if(BooleanUtil.isTrue(saveDB)){
            this.saveBatch(list);
        }
        return this.listBaseByList(list);
    }

    /**
     * 编辑
     *
     * @param resources 文件信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(LocalStorageDO resources) {
        this.updateById(resources);
    }

    /**
     * 多选删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<String> ids) {
        for (String id : ids) {
            LocalStorageDO storage = this.getById(id);
            FileUtil.del(storage.getPath());
        }
        this.removeByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException if any.
     */
    public void download(List<LocalStorageDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDTO localStorageDTO : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("备注名", localStorageDTO.getName());
            map.put("文件类型", localStorageDTO.getType());
            map.put("文件大小", localStorageDTO.getSize());
            map.put("创建者", localStorageDTO.getCreatedBy());
            map.put("创建日期", localStorageDTO.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * <p>
     * 获取文件信息，加密
     * </p>
     *
     * @param ids 加密
     * @return /
     */
    @EncryptMethod(returnVal = true)
    public List<LocalStorageBaseVO> listBaseByEncIds(@EncryptField Set<String> ids) {
        return listBaseByIds(ids);
    }

    /**
     * <p>
     * 获取文件信息
     * </p>
     *
     * @param ids /
     * @return /
     */
    private List<LocalStorageBaseVO> listBaseByIds(Set<String> ids) {
        return this.listBaseByList(this.listByIds(ids));
    }

    /**
     * <p>
     * 获取文件信息
     * </p>
     *
     * @param list /
     * @return /
     */
    private List<LocalStorageBaseVO> listBaseByList(List<LocalStorageDO> list) {
        for (LocalStorageDO localStorageDO : list) {
            String name = localStorageDO.getRealName();
            FileType type = localStorageDO.getType();
            String filePath = StrUtil.format("/file/{}/{}", type.getValue(), name);
            localStorageDO.setPath(filePath);
        }
        return localStorageBaseMapStruct.toVO(list);
    }

}
