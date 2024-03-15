package org.jjche.system.modules.tool.rest;

import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.minio.rest.MinioBaseController;
import org.jjche.minio.util.MinioUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;


/**
 * <p>
 * Minio 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@SysRestController("files")
@ConditionalOnClass({MinioBaseController.class})
public class MinioController extends MinioBaseController {

    public MinioController(MinioUtil minioUtil) {
        super(minioUtil);
    }

}
