package org.jjche.sys.modules.mnt.websocket;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>WebSocketServer class.</p>
 *
 * @author ZhangHouYing
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-10 15:46
 */
@ServerEndpoint("/api/sys/webSocket/{sid}")
@Component
public class WebSocketServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 群发自定义消息
     *
     * @param socketMsg a {@link SocketMsg} object.
     * @param sid       a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public static void sendInfo(SocketMsg socketMsg, @PathParam("sid") String sid) throws IOException {
        String message = JSONUtil.toJsonStr(socketMsg);
        StaticLog.info("推送消息到" + sid + "，推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session a {@link javax.websocket.Session} object.
     * @param sid     a {@link java.lang.String} object.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        //如果存在就先删除一个，防止重复推送消息
        for (WebSocketServer webSocket : webSocketSet) {
            if (webSocket.sid.equals(sid)) {
                webSocketSet.remove(webSocket);
            }
        }
        webSocketSet.add(this);
        this.sid = sid;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session a {@link javax.websocket.Session} object.
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        StaticLog.info("收到来" + sid + "的信息:" + message);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                StaticLog.error(e.getMessage(), e);
            }
        }
    }

    /**
     * <p>onError.</p>
     *
     * @param session a {@link javax.websocket.Session} object.
     * @param error   a {@link java.lang.Throwable} object.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        StaticLog.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(sid, that.sid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }
}
