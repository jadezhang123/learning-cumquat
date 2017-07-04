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
import own.jadezhang.learning.cumquat.zookeeper.context.IProductApplicationContext;
import own.jadezhang.learning.cumquat.zookeeper.context.ProductApplicationContext;

/**
 * 由于zookeeper集群特性与CuratorFramework的重连特性，
 * 单个应用应该保证zookeeper客户端在运行期内的单例特性
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public class ZKClientFactory {

    private final static Logger logger = LoggerFactory.getLogger(ZKClientFactory.class);

    public static final String DEFAULT_CONNECT_STRING = "192.168.17.45:2181";

    public static final String APP_MAIN_CONF_FILE = "config/main-conf.properties";

    private final CuratorFramework client;

    private ZKClientFactory() {
        String connectString = getConnectString();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = createWithOptions(connectString, retryPolicy, 2000, 10000);
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                switch (newState) {
                    case CONNECTED:
                        logger.info("zookeeper CONNECTED");
                        break;
                    case RECONNECTED:
                        logger.info("zookeeper RECONNECTED");
                        break;
                    case LOST:
                        logger.info("zookeeper LOST");
                        break;
                }
            }
        });
        client.start();
    }

    /**
     * 通过应用context获取zookeeper连接信息
     * @return
     */
    private String getConnectString() {
        String connectString = DEFAULT_CONNECT_STRING;
        IProductApplicationContext context = ProductApplicationContext.getInstance();
        //连接集群配置
        String connStr = context.zkConnectString();
        if (StringUtils.isNoneBlank(connStr)) {
            connectString = connStr;
        } else {
            //连接单点配置
            String ip = context.zkIp();
            String port = context.zkPort();
            if (StringUtils.isNoneBlank(ip) && StringUtils.isNoneBlank(port)) {
                connectString = ip + ":" + port;
            }
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
