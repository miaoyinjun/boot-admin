package org.jjche.filter.enc.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.jjche.cache.service.RedisService;
import org.jjche.common.api.CommonApi;
import org.jjche.common.constant.FilterEncConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.common.exception.RequestLimitException;
import org.jjche.common.exception.RequestTimeoutException;
import org.jjche.common.exception.SignException;
import org.jjche.common.exception.WhiteIpException;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.filter.constant.FilterCacheKey;
import org.jjche.filter.enc.api.enums.FilterEncEnum;
import org.jjche.filter.util.EncUtil;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 检查参数
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-14
 */
public class EncCheckHeaderInterceptor implements HandlerInterceptor {
    private CommonApi commonApi;
    private RedisService redisService;
    private AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public EncCheckHeaderInterceptor(CommonApi commonApi, RedisService redisService) {
        this.commonApi = commonApi;
        this.redisService = redisService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String appIdValue = request.getHeader(FilterEncEnum.APP_ID.getKey());
        String timestampValue = request.getHeader(FilterEncEnum.TIMESTAMP.getKey());
        String nonceValue = request.getHeader(FilterEncEnum.NONCE.getKey());
        String signValue = request.getHeader(FilterEncEnum.SIGN.getKey());

        /** appId */
        Assert.notBlank(appIdValue, FilterEncEnum.APP_ID.getErrMsg());
        /** timestamp */
        Assert.notBlank(timestampValue, FilterEncEnum.TIMESTAMP.getErrMsg());
        /** nonce */
        Assert.notBlank(nonceValue, FilterEncEnum.NONCE.getErrMsg());
        /** sign */
        Assert.notBlank(signValue, FilterEncEnum.SIGN.getErrMsg());

        /** 校验随机数长度 */
        Assert.isTrue(StrUtil.length(nonceValue) == FilterEncConstant.NONCE_LENGTH, FilterEncEnum.NONCE.getErrMsg());

        /** 校验时间有效性 */
        Long paramTimestamp = null;
        boolean isNumber = NumberUtil.isNumber(timestampValue);
        if (isNumber) {
            paramTimestamp = Long.valueOf(timestampValue);
        }
        long now = System.currentTimeMillis();
        //允许5分钟前后差值
        boolean isExpireTime = paramTimestamp == null
                || now - paramTimestamp > FilterEncConstant.EXPIRE_TIME
                || paramTimestamp - now > FilterEncConstant.EXPIRE_TIME;
        if (isExpireTime) {
            throw new RequestTimeoutException();
        }

        StaticLog.info("md5Filter.appId:{},timestamp:{}, nonce:{},sign:{}", appIdValue, timestampValue, nonceValue, signValue);

        /** 校验appId有效性 */
        SecurityAppKeyBasicVO appSecretVO = commonApi.getAppKeyByAppId(appIdValue);
        String appSecret = null;
        if (appSecretVO != null) {
            appSecret = appSecretVO.getAppSecret();
        }
        Assert.notNull(appSecret, FilterEncEnum.APP_ID.getErrMsg());

        /** md5生成16进制小写字符串 */
        String mySign = EncUtil.md5Sign(appSecret, timestampValue, nonceValue);
        if (ObjectUtil.notEqual(signValue, mySign)) {
            throw new SignException();
        }
        String encKey = appSecretVO.getEncKey();
        String urls = appSecretVO.getUrls();
        String whiteIp = appSecretVO.getWhiteIp();
        Integer limitCount = appSecretVO.getLimitCount();
        /** url匹配权限验证 */
        this.checkUrl(urls, request);
        /** 白名单验证 */
        this.checkWhiteIp(whiteIp, request);
        /** 限速 */
        this.checkRequestLimit(limitCount, request.getRequestURI());
        //设置加密key到变量
        ContextUtil.setAppKeyEncKey(encKey);
        return true;
    }

    /**
     * <p>
     * url匹配权限验证
     * </p>
     *
     * @param urls    /
     * @param request /
     */
    private void checkUrl(String urls, HttpServletRequest request) {
        List<String> urlList = CollUtil.newArrayList();
        if (StrUtil.isNotBlank(urls)) {
            urlList = StrUtil.split(urls, StrUtil.C_LF);
        }
        String requestURI = request.getRequestURI();
        boolean matchUrl = false;
        for (String url : urlList) {
            matchUrl = PATH_MATCHER.match(url, requestURI);
            if (matchUrl) {
                break;
            }
        }
        if (BooleanUtil.isFalse(matchUrl)) {
            throw new AccessDeniedException(null);
        }
    }

    /**
     * <p>
     * 白名单验证
     * </p>
     *
     * @param whiteIp /
     * @param request /
     */
    private void checkWhiteIp(String whiteIp, HttpServletRequest request) {
        List<String> whiteList = CollUtil.newArrayList();
        if (StrUtil.isNotBlank(whiteIp)) {
            whiteList = StrUtil.split(whiteIp, StrUtil.C_LF);
        }
        if (CollUtil.isNotEmpty(whiteList)) {
            String clientIp = HttpUtil.getIp(request);
            boolean matchWhiteIp = CollUtil.contains(whiteList, clientIp);
            if (BooleanUtil.isFalse(matchWhiteIp)) {
                throw new WhiteIpException();
            }
        }
    }

    /**
     * <p>
     * 限速
     * </p>
     *
     * @param limitCount 速度/秒
     * @param uri        地址
     */
    private void checkRequestLimit(Integer limitCount, String uri) {
        if (limitCount != null && limitCount > 0) {
            String url = uri.replaceAll("/", "_");
            List<Object> keys = CollUtil.toList(StrUtil.join(FilterCacheKey.REQUEST_LIMIT, "_", url));

            RedisScript<Long> redisScript = new DefaultRedisScript<>(FilterCacheKey.SCRIPT_LUA_LIMIT, Long.class);
            Number count = redisService.execute(redisScript, keys, limitCount, 1);
            Boolean isMorThan = null != count && count.intValue() <= limitCount;
            if (BooleanUtil.isFalse(isMorThan)) {
                throw new RequestLimitException();
            }
        }
    }
}
