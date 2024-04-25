package org.jjche.minio.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.IdUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import org.jjche.common.enums.FileType;
import org.jjche.common.util.StrUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.minio.config.MinioConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MinioUtil {
    private final MinioConfig prop;
    private final MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file, String bucketName, String typeName) {
        if (StrUtil.isBlank(bucketName)) {
            bucketName = prop.getBucketName();
        }
        if (StrUtil.isBlank(typeName)) {
            typeName = "default";
        }
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new RuntimeException();
        }

        File fileTemp = FileUtil.toFile(file);

        String fileName = IdUtil.fastSimpleUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String date = DateUtil.formatDate(DateUtil.date());
        String objectName = date + "/" + typeName + "/" + fileName;
        try {
            InputStream fileIs = FileUtil.getInputStream(fileTemp);
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(fileIs, file.getSize(), -1).contentType(file.getContentType()).build();

            //原图上传
            minioClient.putObject(objectArgs);

            //图片类型
            Boolean isPic = FileType.IMAGE.equals(FileUtil.getFileType(FileUtil.getType(fileTemp)));
            if (isPic) {
                int fileSuffixIndex = objectName.lastIndexOf(".");
                String fileSuffix = objectName.substring(fileSuffixIndex);
                String objectThumbName = objectName.substring(0, fileSuffixIndex);
                //缩略图上传
                objectThumbName += "_thumb" + fileSuffix;
                File fileThumb = FileUtil.newFile(fileTemp.getParentFile() + File.separator + objectThumbName);
                FileUtil.copy(fileTemp, fileThumb, true);
                ImgUtil.scale(fileTemp, fileThumb, 0.25f);
                InputStream fileThumbIs = FileUtil.getInputStream(fileThumb);
                long fileThumbSize = FileUtil.size(fileThumb);

                objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectThumbName)
                        .stream(fileThumbIs, fileThumbSize, -1).contentType(file.getContentType()).build();
                minioClient.putObject(objectArgs);

                /**
                 //原图压缩
                 String objectComName = objectName.substring(0, fileSuffixIndex);
                 objectComName += "_com" + fileSuffix;
                 File fileCom = FileUtil.newFile(fileTemp.getParentFile() + File.separator + objectComName);
                 FileUtil.copy(fileTemp, fileCom, true);
                 Img.from(fileTemp)
                 .setQuality(0.4)//压缩比率
                 .write(fileCom);
                 InputStream fileComIs = FileUtil.getInputStream(fileCom);
                 long fileComSize = FileUtil.size(fileCom);

                 objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectComName)
                 .stream(fileComIs, fileComSize, -1).contentType(file.getContentType()).build();
                 minioClient.putObject(objectArgs);
                 */
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String urlPrefix = prop.getUrlPrefix();
        if (StrUtil.isNotBlank(urlPrefix)) {
            objectName = urlPrefix + "/" + bucketName + "/" + objectName;
        }
        return objectName;
    }

    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder().bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
