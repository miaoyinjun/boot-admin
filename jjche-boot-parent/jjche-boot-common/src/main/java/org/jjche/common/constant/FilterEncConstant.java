package org.jjche.common.constant;

/**
 * <p>
 * 加密定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-12
 */
public interface FilterEncConstant {

    /**
     * 应用标识 {@value}
     */
    String APP_ID = "appId";
    /**
     * Unix时间戳(毫秒) {@value}
     */
    String TIMESTAMP = "timestamp";
    /**
     * 随机数 {@value}
     */
    String NONCE = "nonce";
    /**
     * 签名 {@value}
     */
    String SIGN = "sign";

    /**
     * 应用标识 {@value}
     */
    String APP_ID_DESC = "应用标识";
    /**
     * Unix时间戳(毫秒) {@value}
     */
    String TIMESTAMP_DESC = "Unix时间戳(毫秒)，差值不超过5分钟";
    /**
     * 随机数 {@value}
     */
    String NONCE_DESC = "随机数";
    /**
     * 签名 {@value}
     */
    String SIGN_DESC = "签名-md5生成16进制小写(appSecret+timestamp+nonce)";

    /**
     * 参数类型 {@value}
     */
    String PARAM_TYPE_HEADER = "header";

    /**
     * 默认应用标识 {@value}
     */
    String DEFAULT_APP_ID = "default_app_id";

    /**
     * 默认应用密钥 {@value}
     */
    String DEFAULT_APP_SECRET = "default_app_secret";

    /**
     * 超时时效，超过此时间认为签名过期
     */
    Long EXPIRE_TIME = 5 * 60 * 1000L;

    /**
     * 随机数长度
     */
    Integer NONCE_LENGTH = 10;

    /**
     * 过滤器加密属性路径前缀{@value}
     */
    String PROPERTY_ENC_PREFIX = "jjche.filter.enc";

    /**
     * 过滤器加密字段key{@value}
     */
    String PROPERTY_ENC_FIELD_SECRET_KEY = PROPERTY_ENC_PREFIX + ".field.secret-key";

    /**
     * 过滤器过滤路径url{@value}
     */
    String PROPERTY_ENC_API_FILTER = PROPERTY_ENC_PREFIX + ".api";


}