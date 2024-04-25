package org.jjche.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.lang3.ArrayUtils;
import org.jjche.core.constant.EnvConstant;
import org.jjche.common.constant.SpringPropertyConstant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>SpringContextHolder class.</p>
 *
 * @author Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static final List<CallBack> CALL_BACKS = new ArrayList<>();
    private static ApplicationContext applicationContext = null;
    private static boolean addCallback = true;

    /**
     * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
     * 在SpringContextHolder 初始化后，进行回调使用
     *
     * @param callBack 回调函数
     */
//    public synchronized static void addCallBacks(CallBack callBack) {
//        if (addCallback) {
//            SpringContextHolder.CALL_BACKS.add(callBack);
//        } else {
//            log.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
//            callBack.executor();
//        }
//    }

    /**
     * <p>
     * 是否开发环境
     * </p>
     *
     * @return 是否开发环境
     */
    public static boolean isDev() {
        return getEnvActive().equalsIgnoreCase(EnvConstant.DEV);
    }

    /**
     * <p>
     * 是否测试环境
     * </p>
     *
     * @return 是否测试环境
     */
    public static boolean isTest() {
        return getEnvActive().equalsIgnoreCase(EnvConstant.TEST);
    }

    /**
     * <p>
     * 是否预生产环境
     * </p>
     *
     * @return 是否预生产环境
     */
    public static boolean isUat() {
        return getEnvActive().equalsIgnoreCase(EnvConstant.UAT);
    }

    /**
     * <p>
     * 是否生产环境
     * </p>
     *
     * @return 是否生产环境
     */
    public static boolean isProd() {
        return getEnvActive().equalsIgnoreCase(EnvConstant.PROD);
    }

    /**
     * <p>
     * 是否cloud环境
     * </p>
     *
     * @return /
     */
    public static boolean isCloud() {
        return getProperties("jjche.cloud.enabled", Boolean.FALSE, Boolean.class);}

    /**
     * <p>
     * 获取当前环境
     * </p>
     *
     * @return 环境名称
     */
    public static String getEnvActive() {
        String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
        if (!ArrayUtils.isEmpty(profiles)) {
            return profiles[0];
        }
        return "";
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param name a {@link java.lang.String} object.
     * @param <T>  a T object.
     * @return a T object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param requiredType a {@link java.lang.Class} object.
     * @param <T>          a T object.
     * @return a T object.
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param defaultValue 默认值
     * @param requiredType 返回类型
     * @param <T>          a T object.
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
            if (result == null) {
                result = defaultValue;
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property 属性key
     * @return /
     */
    public static String getProperties(String property) {
        return getProperties(property, null, String.class);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param requiredType 返回类型
     * @param <T>          a T object.
     * @return /
     */
    public static <T> T getProperties(String property, Class<T> requiredType) {
        return getProperties(property, null, requiredType);
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new RuntimeException("applicaitonContext属性未注入, 请在applicationContext" +
                    ".xml中定义SpringContextHolder或在SpringBoot启动类中注册SpringContextHolder.");
        }
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    private static void clearHolder() {
        StaticLog.debug("清除SpringContextHolder中的ApplicationContext:"
                + applicationContext);
        applicationContext = null;
    }

    /**
     * <p>
     * 输出项目基本信息
     * </p>
     *
     * @param application main
     */
    public static void appLog(ConfigurableApplicationContext application) {
        try {
            Environment env = application.getEnvironment();
            String serverPort = env.getProperty("server.port");
            String contextPath = Optional
                    .ofNullable(env.getProperty("server.servlet.context-path"))
                    .filter(StrUtil::isNotBlank)
                    .orElse("/");
            String hostAddress = "localhost";
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                StaticLog.warn("The host name could not be determined, using `localhost` as fallback");
            }

            StaticLog.info("\n----------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\thttp://localhost:{}{}\n\t" +
                            "External: \thttp://{}:{}{}\n\t" +
                            "Doc:       \thttp://{}:{}{}sba/api/doc.html\n\t" +
                            "Profile(s): {}\n----------------------------------------------------------",
                    env.getProperty(SpringPropertyConstant.APP_NAME),
                    serverPort,
                    contextPath,
                    hostAddress,
                    serverPort,
                    contextPath,
                    hostAddress,
                    serverPort,
                    contextPath,
                    env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        SpringContextHolder.clearHolder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextHolder.applicationContext != null) {
            StaticLog.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
        if (addCallback) {
            for (CallBack callBack : SpringContextHolder.CALL_BACKS) {
                callBack.executor();
            }
            CALL_BACKS.clear();
        }
        SpringContextHolder.addCallback = false;
    }
}
