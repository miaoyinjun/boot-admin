package org.jjche.cat.integration.feign;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.jjche.cat.constant.CatConstantsExt;
import org.jjche.cat.constant.CatContext;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 * <p>
 * Feign拦截器，Cat消息树生成
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-20
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Override
    public void apply(RequestTemplate template) {
        feignCat(template);
        CatContext catContext = new CatContext();
        Cat.logRemoteCallClient(catContext, Cat.getManager().getDomain());
        template.header(CatConstantsExt.CAT_HTTP_HEADER_ROOT_MESSAGE_ID, catContext.getProperty(Cat.Context.ROOT));
        template.header(CatConstantsExt.CAT_HTTP_HEADER_PARENT_MESSAGE_ID, catContext.getProperty(Cat.Context.PARENT));
        template.header(CatConstantsExt.CAT_HTTP_HEADER_CHILD_MESSAGE_ID, catContext.getProperty(Cat.Context.CHILD));
        template.header(SecurityConstant.FEIGN_SERVICE_NAME, applicationName);
    }

    /**
     * <p>
     * 无法获取执行结果
     * </p>
     *
     * @param template
     */
    private void feignCat(RequestTemplate template) {
        String feignName = template.feignTarget().name();
        String baseUrl = template.feignTarget().url();
        String method = template.method();
        String url = StrUtil.format("{}:{}{}[{}]", method, baseUrl, template.url(), feignName);
        Transaction transaction = Cat.newTransaction(CatConstantsExt.TYPE_CALL_FEIGN, url);
        try {
            transaction.setStatus(Message.SUCCESS);
        } catch (Exception e) {
            Cat.logError(e);
            transaction.setStatus(e.getClass().getSimpleName());
        } finally {
            transaction.complete();
        }
    }

}