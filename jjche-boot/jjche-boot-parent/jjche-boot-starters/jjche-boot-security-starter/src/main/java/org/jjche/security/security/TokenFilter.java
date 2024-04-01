package org.jjche.security.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.BooleanUtil;
import org.jjche.common.api.CommonApi;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.common.dto.JwtUserDTO;
import org.jjche.common.vo.UserVO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.vo.DataScopeVO;
import org.jjche.common.wrapper.HeaderMapRequestWrapper;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.constant.FileConstant;
import org.jjche.core.runner.RequestMappingRunner;
import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>TokenFilter class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
public class TokenFilter extends GenericFilterBean {
    private final CommonApi commonApi;

    /**
     * <p>Constructor for TokenFilter.</p>
     *
     * @param commonApi api
     */
    public TokenFilter(CommonApi commonApi) {
        this.commonApi = commonApi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        String uri = request.getRequestURI();
        String method = request.getMethod();

        //验证url和method是否存在
        if (validUrl(response, uri, method)) {
            return;
        }

        HeaderMapRequestWrapper reqWrapper = new HeaderMapRequestWrapper((HttpServletRequest) servletRequest);
        //非cloud才会走这里
        JwtUserDTO userDetails = commonApi.getUserDetails();
        String token = reqWrapper.getHeader(SecurityConstant.HEADER_AUTH);
        if (userDetails != null) {
            Authentication authentication = null;
            UserVO userVO = userDetails.getUser();
            UserTypeEnum userType = userVO.getUserType();
            //设置用户信息上下文
            setContextUser(userDetails, token);
            //密码
            if (UserTypeEnum.PWD == userType) {
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }//短信
            else if (UserTypeEnum.SMS == userType) {
                authentication = new SmsCodeAuthenticationToken(userVO.getUsername());
            }
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(reqWrapper, servletResponse);
    }

    /**
     * <p>
     * 验证url和method是否存在
     * </p>
     *
     * @param response /
     * @param uri      /
     * @param method   /
     * @return /
     */
    private boolean validUrl(HttpServletResponse response, String uri, String method) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean fileAvatarUri = antPathMatcher.match(FileConstant.AVATAR_PATH_MATCH, uri)
                || antPathMatcher.match(FileConstant.FILE_PATH_MATCH, uri);
        if(fileAvatarUri){
            return false;
        }
        boolean isMachUrl = false;
        boolean isMachUrlMethod = false;
        for (PatternsRequestCondition p : RequestMappingRunner.MAPPING_INFO_MAP.keySet()) {
            if (p.getMatchingPatterns(uri).size() > 0) {
                isMachUrl = true;
                Set<String> methods = RequestMappingRunner.MAPPING_INFO_MAP.get(p);
                if (CollUtil.isNotEmpty(methods)) {
                    isMachUrlMethod = methods.contains(method);
                    // fix /test/{id} /test/import 匹配问题
                } else {
                    //fix @RequestMapping("/bpm/**")没有methods问题
                    isMachUrlMethod = true;
                }
                if (isMachUrlMethod) {
                    break;
                }
            }
        }
        if (BooleanUtil.isFalse(isMachUrl)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            HttpUtil.printJson(response, R.notFound());
            return true;
        }
        if (BooleanUtil.isTrue(isMachUrl) && BooleanUtil.isFalse(isMachUrlMethod)) {
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            HttpUtil.printJson(response, R.methodNotAllowed());
            return true;
        }
        return false;
    }

    /**
     * <p>
     * 设置用户信息上下文
     * </p>
     *
     * @param userDetails 用户
     * @param token       /
     */
    private void setContextUser(JwtUserDTO userDetails, String token) {
        if (userDetails != null) {
            ContextUtil.setToken(token);
            UserVO user = userDetails.getUser();
            //用户基本信息
            Long userId = user.getId();
            String username = user.getUsername();
            Set<String> elPermissions = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            //数据范围
            DataScopeVO dataScopeVO = userDetails.getDataScopeVO();
            //部门id
            Set<Long> dataScopeDeptIds = dataScopeVO.getDeptIds();
            //全部
            Boolean dataScopeIsAll = dataScopeVO.isAll();
            //本人
            Boolean dataScopeIsSelf = dataScopeVO.isSelf();
            //用户id
            Long dataScopeUserid = dataScopeVO.getUserId();
            //用户名
            String dataScopeUsername = dataScopeVO.getUserName();

            ContextUtil.setUserId(userId);
            ContextUtil.setUsername(username);
            ContextUtil.setPermissions(elPermissions);
            //数据范围
            ContextUtil.setDataScopeDeptIds(dataScopeDeptIds);
            ContextUtil.setDataScopeIsAll(dataScopeIsAll);
            ContextUtil.setDataScopeIsSelf(dataScopeIsSelf);
            ContextUtil.setDataScopeUserId(dataScopeUserid);
            ContextUtil.setDataScopeUserName(dataScopeUsername);
        }
    }
}
