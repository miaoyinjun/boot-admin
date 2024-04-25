package org.jjche.sys.modules.tool.rest;

import org.jjche.minio.rest.MinioBaseController;
import org.jjche.minio.util.MinioUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("files")
@ConditionalOnClass({MinioBaseController.class})
public class MinioController extends MinioBaseController {

    public MinioController(MinioUtil minioUtil) {
        super(minioUtil);
    }

}
