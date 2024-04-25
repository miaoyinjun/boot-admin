package org.jjche.core.util;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.common.util.ThrowableUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-15
 */
public class LogUtil {

    /**
     * <p>
     * 获取请求客户端信息
     * </p>
     *
     * @param logRecord 日志
     */
    public static void setLogRecordHttpRequest(LogRecordDTO logRecord) {
        try {
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            String ip = HttpUtil.getIp(request);
            String ua = request.getHeader(HttpHeaders.USER_AGENT);
            UserAgent userAgent = UserAgentUtil.parse(ua);
            logRecord.setBrowser(HttpUtil.getBrowser(userAgent));
            logRecord.setOs(HttpUtil.getOs(userAgent));
            logRecord.setUserAgent(ua);
            logRecord.setRequestIp(ip);

            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                logRecord.setUrl(wrapper.getRequestURI());
                if (!ServletUtil.isMultipart(wrapper) && logRecord.getSaveParams()) {
                    Map<String, Object> paramMap = new HashMap<>(2);
                    Map<String, String> paramsQueryMap = ServletUtil.getParamMap(wrapper);
                    //参数值
                    if (!ServletUtil.isMultipart(wrapper) && wrapper.getContentLength() > 0) {
                        String body = StrUtil.str(wrapper.getContentAsByteArray(), Charset.defaultCharset());
                        if (StrUtil.isNotBlank(body)) {
                            boolean isTypeJSON = JSONUtil.isTypeJSON(body);
                            Object bodyJSON = body;
                            if (isTypeJSON) {
                                bodyJSON = JSONUtil.parse(body);
                            }
                            paramMap.put("body", bodyJSON);
                        }
                    }
                    paramMap.put("query", paramsQueryMap);
                    String paramStr = JSONUtil.toJsonPrettyStr(paramMap);
                    logRecord.setParams(paramStr);
                }
            }
        } catch (Exception e) {
            StaticLog.error("setLogRecordHttpRequest:{}", ThrowableUtil.getStackTrace(e));
        }
    }
}
