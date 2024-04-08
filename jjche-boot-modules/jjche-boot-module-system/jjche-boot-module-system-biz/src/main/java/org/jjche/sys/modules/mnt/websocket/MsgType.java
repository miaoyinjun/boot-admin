package org.jjche.sys.modules.mnt.websocket;

/**
 * <p>MsgType class.</p>
 *
 * @author ZhangHouYing
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-10 9:56
 */
public enum MsgType {
    /**
     * 连接
     */
    CONNECT,
    /**
     * 关闭
     */
    CLOSE,
    /**
     * 信息
     */
    INFO,
    /**
     * 错误
     */
    ERROR
}
