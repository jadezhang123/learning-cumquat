package own.jadezhang.learning.cumquat.zookeeper.curator;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 由于zookeeper集群特性与CuratorFramework的重连特性，
 * 单个应用应该保证运行期zookeeper客户端的单例特性
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public class ZKClientFactory {

    private final static Logger logger = LoggerFactory.getLogger(ZKClientFactory.class);

    public static final String DEFAULT_CONNECT_STRING = "192.168.17.45:2181";

    public static final String APP_MAIN_CONF_FILE = "config/main-conf.properties";

    private final CuratorFramework client;

    private ZKClientFactory() {
        String connectString = loadConfigFile();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = createWithOptions(connectString, retryPolicy, 2000, 10000);
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                switch (newState) {
                    case CONNECTED:
                    case RECONNECTED:
                        break;
                    case LOST:
                        break;
                }
            }
        });
        client.start();
    }

    /**
     * 加载应用信息配置文件获取zookeeper连接信息
     * @return
     */
    private String loadConfigFile() {
        String connectString = DEFAULT_CONNECT_STRING;
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(APP_MAIN_CONF_FILE);
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            //连接集群配置
            String connStr = properties.getProperty("zk.connectString");
            if (StringUtils.isNoneBlank(connStr)) {
                connectString = connStr;
            }else {
                //连接单点配置
                String ip = properties.getProperty("zk.ip");
                String port = properties.getProperty("zk.port");
                if (StringUtils.isNoneBlank(ip) && StringUtils.isNoneBlank(port)) {
                    connectString = ip + ":" + port;
                }
            }
        } catch (Exception e) {
            logger.error("load main config file [{}] from classpath occurred a error : {}", APP_MAIN_CONF_FILE, e.getMessage());
        }
        logger.info("using connectString[{}] to connect zookeeper", connectString);
        return connectString;
    }

    private CuratorFramework createWithOptions(String connectString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    private static class ZKClientHolder {
        private static final ZKClientFactory instance = new ZKClientFactory();
    }

    /**
     * 获取zk客户端实例（单例）
     * @return
     */
    public static CuratorFramework getClient() {
        return ZKClientHolder.instance.client;
    }
}
