package org.jjche.cat.conf;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import com.dianping.cat.configuration.ClientConfigProvider;
import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.entity.Server;
import net.dreamlu.mica.core.utils.StringUtil;
import org.jjche.cat.property.JjcheCatProperties;
import org.jjche.cat.constant.CatConstant;
import org.jjche.common.constant.SpringPropertyConstant;

/**
 * <p>
 * CAT SPI加载配置
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-17
 */
public class CatClientConfigProvider implements ClientConfigProvider {
    private static JjcheCatProperties catProperties;

    public static void setCatProperties(JjcheCatProperties catProperties) {
        CatClientConfigProvider.catProperties = catProperties;
    }

    @Override
    public ClientConfig getClientConfig() {
        //ip地址
        String serverStr = catProperties.getServers();
        if (StringUtil.isBlank(serverStr)) {
            return null;
        }
        ClientConfig config = new ClientConfig();
        if (StringUtil.isNotBlank(serverStr)) {
            String[] servers = serverStr.split(CatConstant.SERVER_DELIMITER);
            for (String server : servers) {
                if (StrUtil.isEmpty(server)) {
                    continue;
                }
                String[] params = server.split(CatConstant.PORT_DELIMITER);
                if (params.length != 3) {
                    StaticLog.error("cat server配置格式有误");
                    continue;
                }
                String ip = params[0];
                int httpPort = Integer.parseInt(params[1]);
                int port = Integer.parseInt(params[2]);
                config.addServer(new Server(ip).setHttpPort(httpPort).setPort(port));
            }
            //应用名
            String appName = SpringUtil.getProperty(SpringPropertyConstant.APP_NAME);
            config.setDomain(appName);
        }
        return config;
    }
}
