package org.jjche.tool.modules.tool.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.tool.modules.tool.api.dto.QiniuQueryCriteriaDTO;
import org.jjche.tool.modules.tool.constant.ToolCacheKey;
import org.jjche.tool.modules.tool.domain.QiniuConfigDO;
import org.jjche.tool.modules.tool.domain.QiniuContentDO;
import org.jjche.tool.modules.tool.mapper.QiNiuConfigMapper;
import org.jjche.tool.modules.tool.mapper.QiniuContentMapper;
import org.jjche.tool.modules.tool.utils.QiNiuUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>QiNiuService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-31
 */
@Service
@RequiredArgsConstructor
public class QiNiuService extends MyServiceImpl<QiNiuConfigMapper, QiniuConfigDO> {

    private final QiniuContentMapper qiniuContentRepository;

    @Value("${jjche.tool.qiniu.max-size}")
    private Long maxSize;

    /**
     * 查配置
     *
     * @return QiniuConfigDO
     */
    @Cached(name = ToolCacheKey.QI_NIU)
    public QiniuConfigDO find() {
        return this.getById(1L);
    }

    /**
     * 修改配置
     *
     * @param qiniuConfig 配置
     * @return QiniuConfigDO
     */
    @CacheInvalidate(name = ToolCacheKey.QI_NIU)
    @Transactional(rollbackFor = Exception.class)
    public QiniuConfigDO config(QiniuConfigDO qiniuConfig) {
        qiniuConfig.setId(1L);
        String http = "http://", https = "https://";
        Boolean isHttpOrHttps = qiniuConfig.getHost().toLowerCase().startsWith(http) || qiniuConfig.getHost().toLowerCase().startsWith(https);
        Assert.isTrue(isHttpOrHttps, "外链域名必须以http://或者https://开头");
        this.saveOrUpdate(qiniuConfig);
        return qiniuConfig;
    }


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(QiniuQueryCriteriaDTO criteria) {
        return MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(QiniuQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.page(pageable, queryWrapper);
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<QiniuContentDO> queryAll(QiniuQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }


    /**
     * <p>
     * 根据文件名查询
     * </p>
     *
     * @param name 文件名
     * @return /
     */
    private QiniuContentDO findByName(String name) {
        LambdaQueryWrapper<QiniuContentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QiniuContentDO::getName, name);
        return qiniuContentRepository.selectOne(queryWrapper);
    }

    /**
     * 上传文件
     *
     * @param file        文件
     * @param qiniuConfig 配置
     * @return QiniuContentDO
     */
    @Transactional(rollbackFor = Exception.class)
    public QiniuContentDO upload(MultipartFile file, QiniuConfigDO qiniuConfig) {
        FileUtil.checkSize(maxSize, file.getSize());
        Assert.notNull(qiniuConfig, "请先添加相应配置，再操作");
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            String key = file.getOriginalFilename();
            QiniuContentDO content = this.findByName(key);
            if (content != null) {
                key = QiNiuUtil.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果

            DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
            content = this.findByName(FileUtil.getFileNameNoEx(putRet.key));
            if (content == null) {
                //存入数据库
                QiniuContentDO qiniuContent = new QiniuContentDO();
                qiniuContent.setSuffix(FileUtil.getExtensionName(putRet.key));
                qiniuContent.setBucket(qiniuConfig.getBucket());
                qiniuContent.setType(qiniuConfig.getType());
                qiniuContent.setName(FileUtil.getFileNameNoEx(putRet.key));
                qiniuContent.setUrl(qiniuConfig.getHost() + "/" + putRet.key);
                qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(file.getSize() + "")));
                qiniuContentRepository.insert(qiniuContent);
                return qiniuContent;
            }
            return content;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 查询文件
     *
     * @param id 文件ID
     * @return QiniuContentDO
     */
    public QiniuContentDO findByContentId(Long id) {
        QiniuContentDO qiniuContent = qiniuContentRepository.selectById(id);
        ValidationUtil.isNull(qiniuContent.getId(), "QiniuContentDO", "id", id);
        return qiniuContent;
    }

    /**
     * 下载文件
     *
     * @param content 文件信息
     * @param config  配置
     * @return String
     */
    public String download(QiniuContentDO content, QiniuConfigDO config) {
        String finalUrl;
        String type = "公开";
        if (type.equals(content.getType())) {
            finalUrl = content.getUrl();
        } else {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            // 1小时，可以自定义链接过期时间
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }

    /**
     * 删除文件
     *
     * @param content 文件
     * @param config  配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(QiniuContentDO content, QiniuConfigDO config) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(content.getBucket(), content.getName() + "." + content.getSuffix());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        qiniuContentRepository.deleteById(content.getId());
    }

    /**
     * 同步数据
     *
     * @param config 配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(QiniuConfigDO config) {
        Assert.notNull(config.getId(), "请先添加相应配置，再操作");
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            QiniuContentDO qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if (this.findByName(FileUtil.getFileNameNoEx(item.key)) == null) {
                    qiniuContent = new QiniuContentDO();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(item.fsize + "")));
                    qiniuContent.setSuffix(FileUtil.getExtensionName(item.key));
                    qiniuContent.setName(FileUtil.getFileNameNoEx(item.key));
                    qiniuContent.setType(config.getType());
                    qiniuContent.setBucket(config.getBucket());
                    qiniuContent.setUrl(config.getHost() + "/" + item.key);
                    qiniuContentRepository.insert(qiniuContent);
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param ids    文件ID数组
     * @param config 配置
     */
    public void deleteAll(Long[] ids, QiniuConfigDO config) {
        for (Long id : ids) {
            delete(findByContentId(id), config);
        }
    }

    /**
     * 更新数据
     *
     * @param type 类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String type) {
        UpdateWrapper updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("type", type);
        this.update(updateWrapper);
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void downloadList(List<QiniuContentDO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QiniuContentDO content : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", content.getName());
            map.put("文件类型", content.getSuffix());
            map.put("空间名称", content.getBucket());
            map.put("文件大小", content.getSize());
            map.put("空间类型", content.getType());
            map.put("创建日期", content.getGmtModified());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
