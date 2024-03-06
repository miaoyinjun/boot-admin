package org.jjche.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.JwtUserDTO;
import org.jjche.common.dto.UserVO;
import org.jjche.common.vo.DataScopeVO;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * http工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
public class HttpUtil extends cn.hutool.http.HttpUtil {

    public static final String UNKNOWN = "unknown";
    private static final Supplier<Stream<String>> BROWSER = () -> Stream.of(
            "Chrome", "Firefox", "Microsoft Edge", "Safari", "Opera"
    );
    private static final Supplier<Stream<String>> OPERATING_SYSTEM = () -> Stream.of(
            "Android", "Linux", "Mac OS X", "Ubuntu", "Windows 10", "Windows 8", "Windows 7", "Windows XP", "Windows Vista"
    );

    /**
     * IP归属地查询
     */
    public static final String IP_JSON_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    /**
     * 注入bean
     */
    private final static Ip2regionSearcher IP_SEARCHER = SpringUtil.getBean(Ip2regionSearcher.class);
    private static boolean ipLocal = true;

    private static String simplifyOperatingSystem(String operatingSystem) {
        return OPERATING_SYSTEM.get().parallel().filter(b -> StrUtil.containsIgnoreCase(operatingSystem, b)).findAny().orElse(operatingSystem);
    }

    private static String simplifyBrowser(String browser) {
        return BROWSER.get().parallel().filter(b -> StrUtil.containsIgnoreCase(browser, b)).findAny().orElse(browser);
    }


    /**
     * 获取ip地址
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (cn.hutool.core.util.StrUtil.contains(ip, comma)) {
            ip = ip.split(",")[0];
        }
        if (cn.hutool.core.util.StrUtil.equals(localhost, ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                StaticLog.error(e.getMessage(), e);
            }
        }
        return ip == null ? UNKNOWN : ip;
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getCityInfo(String ip) {
        if (ipLocal) {
            return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(IP_JSON_URL, ip);
        JSONObject object = JSONUtil.parseObj(cn.hutool.http.HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getLocalCityInfo(String ip) {
        try {
            IpInfo ipInfo = IP_SEARCHER.memorySearch(ip);
            if (ipInfo != null) {
                return ipInfo.getAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>getBrowser.</p>
     * <p>
     * userAgent 请求
     *
     * @param userAgent a {@link cn.hutool.http.useragent.UserAgent} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getBrowser(UserAgent userAgent) {
        Browser browser = userAgent.getBrowser();
        if (browser != null) {
            return simplifyBrowser(browser.getName());
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 获取当前机器的IP
     *
     * @return /
     */
    public static String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface anInterface = interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddresses.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return UNKNOWN;
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    /**
     * <p>
     * 获取系统版本
     * </p>
     *
     * @param userAgent 请求
     * @return 系统版本
     */
    public static String getOs(UserAgent userAgent) {
        OS os = userAgent.getOs();
        if (os != null) {
            return simplifyOperatingSystem(os.getName());
        } else {
            return UNKNOWN;
        }
    }

    /**
     * <p>printJson.</p>
     *
     * @param servletResponse a {@link javax.servlet.http.HttpServletResponse} object.
     * @param result          a {@link java.lang.Object} object.
     */
    @SneakyThrows
    public static void printJson(HttpServletResponse servletResponse, Object result) {
        PrintWriter writer = null;
        OutputStreamWriter osw = null;
        try {
            servletResponse.setContentType("application/json");
            osw = new OutputStreamWriter(servletResponse.getOutputStream(),
                    StandardCharsets.UTF_8);
            writer = new PrintWriter(osw, true);
            String jsonStr = JSONUtil.toJsonStr(result);
            writer.write(jsonStr);
            writer.flush();
            writer.close();
            osw.close();
        } catch (UnsupportedEncodingException ue) {
        } catch (IOException e) {

        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {

                }
            }
        }
        return;
    }

    /**
     * <p>
     * 设置header的用户信息
     * </p>
     *
     * @param userDetails /
     * @return /
     */
    public static Map<String, Object> getUserHeaders(JwtUserDTO userDetails) {
        Map<String, Object> httpHeaders = new HashMap<>();
        if (userDetails != null) {
            UserVO user = userDetails.getUser();
            //用户基本信息
            String userId = String.valueOf(user.getId());
            String username = user.getUsername();
            List<String> elPermissions = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            //数据范围
            DataScopeVO dataScopeVO = userDetails.getDataScopeVO();
            //部门id
            Set<Long> dataScopeDeptIds = dataScopeVO.getDeptIds();
            List<String> dataScopeDeptIdsList = new ArrayList<>();
            if (CollUtil.isNotEmpty(dataScopeDeptIds)) {
                dataScopeDeptIdsList = dataScopeDeptIds.stream().map(o -> String.valueOf(o)).collect(Collectors.toList());
            }
            List<String> dataScopeFinalDeptIdsList = dataScopeDeptIdsList;
            //全部
            String dataScopeIsAll = String.valueOf(dataScopeVO.isAll());
            //本人
            String dataScopeIsSelf = String.valueOf(dataScopeVO.isSelf());
            //用户id
            Long userIdL = dataScopeVO.getUserId();
            String dataScopeUserid = null;
            if (userIdL != null) {
                dataScopeUserid = String.valueOf(userIdL);
            }
            //用户名
            String dataScopeUsername = dataScopeVO.getUserName();

            httpHeaders.put(SecurityConstant.JWT_KEY_USER_ID, userId);
            httpHeaders.put(SecurityConstant.JWT_KEY_USERNAME, username);
            httpHeaders.put(SecurityConstant.JWT_KEY_PERMISSION, elPermissions);
            //数据范围
            httpHeaders.put(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS, dataScopeFinalDeptIdsList);
            httpHeaders.put(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL, dataScopeIsAll);
            httpHeaders.put(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF, dataScopeIsSelf);
            if (dataScopeUserid != null) {
                httpHeaders.put(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID, dataScopeUserid);
            }
            if (dataScopeUsername != null) {
                httpHeaders.put(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME, dataScopeUsername);
            }
        }
        return httpHeaders;
    }
}
