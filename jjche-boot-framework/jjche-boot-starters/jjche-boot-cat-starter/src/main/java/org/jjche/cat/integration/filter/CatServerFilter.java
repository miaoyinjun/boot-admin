package org.jjche.cat.integration.filter;


import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.commons.io.IOUtils;
import org.jjche.cat.constant.CatConstantsExt;
import org.jjche.cat.constant.CatContext;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * Cat 监控过滤器
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-20
 */
public class CatServerFilter implements Filter {

    private String applicationName;

    public CatServerFilter(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ContentCachingRequestWrapper wrapperRequest = new ContentCachingRequestWrapper(request);
        String uri = request.getRequestURI();

        // 构建远程消息树
        if (request.getHeader(CatConstantsExt.CAT_HTTP_HEADER_ROOT_MESSAGE_ID) != null) {
            CatContext catContext = new CatContext();
            catContext.addProperty(Cat.Context.ROOT, request.getHeader(CatConstantsExt.CAT_HTTP_HEADER_ROOT_MESSAGE_ID));
            catContext.addProperty(Cat.Context.PARENT, request.getHeader(CatConstantsExt.CAT_HTTP_HEADER_PARENT_MESSAGE_ID));
            catContext.addProperty(Cat.Context.CHILD, request.getHeader(CatConstantsExt.CAT_HTTP_HEADER_CHILD_MESSAGE_ID));
            Cat.logRemoteCallServer(catContext);
        }

        Transaction filterTransaction = Cat.newTransaction(CatConstants.TYPE_URL, request.getMethod() + ":" + uri);

        try {
            Cat.logEvent(CatConstantsExt.TYPE_URL_METHOD, request.getMethod(), Message.SUCCESS, request.getRequestURL().toString());
            Cat.logEvent(CatConstantsExt.TYPE_URL_CLIENT, request.getRemoteHost() + "[" + applicationName + "]");

            filterChain.doFilter(wrapperRequest, servletResponse);
            filterTransaction.setSuccessStatus();
        } catch (Exception e) {
            filterTransaction.setStatus(e);
            Cat.logError("reqBody：" + getRequestBody(wrapperRequest), e);
            throw e;
        } finally {
            filterTransaction.complete();
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper req) {
        try {
            return IOUtils.toString(req.getContentAsByteArray(), "UTF-8");
        } catch (IOException e) {

        }
        return "";
    }

    @Override
    public void destroy() {

    }
}