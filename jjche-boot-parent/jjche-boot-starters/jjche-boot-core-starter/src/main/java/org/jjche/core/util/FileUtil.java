package org.jjche.core.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.log.StaticLog;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * File工具类，扩展 hutool 工具包
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-24
 */
public class FileUtil extends org.jjche.common.util.FileUtil {
    /**
     * MultipartFile转File
     *
     * @param multipartFile a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link java.io.File} object.
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = new File(SYS_TEM_DIR + IdUtil.simpleUUID() + prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            StaticLog.error(e.getMessage(), e);
        }
        return file;
    }

    /**
     * 将文件名解析成文件的上传路径
     *
     * @param file     a {@link org.springframework.web.multipart.MultipartFile} object.
     * @param filePath a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    public static File upload(MultipartFile file, String filePath) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String name = getFileNameNoEx(file.getOriginalFilename());
        String suffix = getExtensionName(file.getOriginalFilename());
        String nowStr = "-" + format.format(date);
        try {
            String fileName = name + nowStr + "." + suffix;
            String path = filePath + fileName;
            // getCanonicalFile 可解析正确各种路径
            File dest = new File(path).getCanonicalFile();
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    StaticLog.warn("was not successful.");
                }
            }
            // 文件写入
            file.transferTo(dest);
            return dest;
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
        }
        return null;
    }


}
