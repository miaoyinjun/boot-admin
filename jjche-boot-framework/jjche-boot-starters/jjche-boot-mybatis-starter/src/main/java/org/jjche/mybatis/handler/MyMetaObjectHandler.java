package org.jjche.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.jjche.core.util.SecurityUtil;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自动填充，创建、修改人字段
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-14
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String username = SecurityUtil.getUsernameOrDefaultUsername();
        this.strictInsertFill(metaObject, "createdBy", String.class, username);
        this.updateFill(metaObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String username = SecurityUtil.getUsernameOrDefaultUsername();
        this.strictUpdateFill(metaObject, "updatedBy", String.class, username);
    }
}
