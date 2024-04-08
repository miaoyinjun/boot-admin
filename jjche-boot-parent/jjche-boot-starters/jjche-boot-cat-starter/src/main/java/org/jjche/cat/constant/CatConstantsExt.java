package org.jjche.cat.constant;

import com.dianping.cat.CatConstants;

/**
 * <p>
 * Cat 常量
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-20
 */
public class CatConstantsExt extends CatConstants {

    public static final String CAT_HTTP_HEADER_CHILD_MESSAGE_ID = "X-CAT-CHILD-ID";
    public static final String CAT_HTTP_HEADER_PARENT_MESSAGE_ID = "X-CAT-PARENT-ID";
    public static final String CAT_HTTP_HEADER_ROOT_MESSAGE_ID = "X-CAT-ROOT-ID";
    public static final String TYPE_URL_METHOD = "URL.Method";
    public static final String TYPE_URL_CLIENT = "URL.Client";
    public static final String TYPE_URL_PARAMS = "PARAMS";
    public static final String TYPE_CALL_REST_TEMPLATE = "HttpCall.RestTemplate";
    public static final String TYPE_CALL_FEIGN = "HttpCall.Feign";
}