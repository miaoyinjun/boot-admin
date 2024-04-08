package org.jjche.file.modules.minio.rest;

import org.jjche.minio.rest.MinioBaseController;
import org.jjche.minio.util.MinioUtil;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * Minio 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@RestController
public class CloudMinioController extends MinioBaseController {

    public CloudMinioController(MinioUtil minioUtil) {
        super(minioUtil);
    }

}
