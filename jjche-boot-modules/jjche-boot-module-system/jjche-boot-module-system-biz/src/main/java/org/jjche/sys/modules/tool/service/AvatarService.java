package org.jjche.sys.modules.tool.service;

import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.FileType;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.core.fileconf.FileProperties;
import org.jjche.core.util.FileUtil;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * 头像
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-11-02
 */
@Service
@RequiredArgsConstructor
public class AvatarService {
    private final FileProperties properties;

    /**
     * <p>
     * 上传
     * </p>
     *
     * @param multipartFile 文件
     * @return /
     */
    public File uploadAvatar(MultipartFile multipartFile) {
        // 判断文件是否为图片
        AssertUtil.notNull(multipartFile, SysErrorCodeEnum.FILE_PIC_ERROR);
        // 文件大小验证
        FileUtil.checkSize(properties.getAvatarMaxSize(), multipartFile.getSize());

        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        Boolean isPic = FileType.IMAGE.equals(FileUtil.getFileType(suffix));
        AssertUtil.isTrue(isPic, SysErrorCodeEnum.FILE_PIC_ERROR);
        return FileUtil.upload(multipartFile, properties.getPath().getAvatar());
    }
}
