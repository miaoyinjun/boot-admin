package org.jjche.system.modules.app.function;

import org.jjche.common.context.LogRecordContext;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.jjche.system.modules.app.domain.SecurityAppKeyDO;
import org.jjche.system.modules.app.mapstruct.SecurityAppKeyMapStruct;
import org.jjche.system.modules.app.service.SecurityAppKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 应用密钥 设置修改/删除前的数据到变量
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Component
public class SecurityAppKeyDiffOldByIdParseFunction implements IParseFunction {
    @Autowired
    @Lazy
    private SecurityAppKeyService securityAppKeyService;
    @Autowired
    @Lazy
    private SecurityAppKeyMapStruct securityAppKeyMapStruct;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeBefore() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String functionName() {
        return "SECURITY_APP_KEY_DIFF_OLD_BY_ID";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Object idObj) {
        Object result = null;
        if (idObj instanceof List) {
            List<Long> ids = (List<Long>) idObj;
            List<SecurityAppKeyDO> list = this.securityAppKeyService.listByIds(ids);
            result = this.securityAppKeyMapStruct.toDTO(list);
        } else {
            Long id = (Long) idObj;
            SecurityAppKeyDO securityAppKeyDO = this.securityAppKeyService.getById(id);
            result = this.securityAppKeyMapStruct.toDTO(securityAppKeyDO);
        }
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, result);
        return null;
    }
}