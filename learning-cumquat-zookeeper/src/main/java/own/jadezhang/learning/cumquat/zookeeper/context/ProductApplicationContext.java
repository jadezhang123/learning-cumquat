package own.jadezhang.learning.cumquat.zookeeper.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Zhang Junwei on 2017/7/4.
 */
public class ProductApplicationContext implements IProductApplicationContext {

    public static final String APP_MAIN_CONF_FILE = "config/main-conf.properties";
    public static final String CONF_PRODUCT = "product";
    public static final String CONF_APP = "app";
    public static final String CONF_APP_Name = "appName";
    public static final String CONF_ZK_IP = "zk.ip";
    public static final String CONF_ZK_PORT = "zk.port";
    public static final String CONF_ZK_CONNECT_STRING = "zk.connectString";
    public static final String CONF_BACKUP_DIR = "backupDir";
    private static final Logger logger = LoggerFactory.getLogger(ProductApplicationContext.class);
    private String product;
    private String app;
    private String appName;
    private String zkIp;
    private String zkPort;
    private String zkConnectString;
    private String backupDir;

    private ProductApplicationContext() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(APP_MAIN_CONF_FILE);
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            product = properties.getProperty(CONF_PRODUCT);
            app = properties.getProperty(CONF_APP);
            appName = properties.getProperty(CONF_APP_Name);
            zkIp = properties.getProperty(CONF_ZK_IP);
            zkPort = properties.getProperty(CONF_ZK_PORT);
            zkConnectString = properties.getProperty(CONF_ZK_CONNECT_STRING);
            backupDir = properties.getProperty(CONF_BACKUP_DIR);
        } catch (IOException e) {
            logger.error("load main config file [{}] from classpath occurred a error : {}", APP_MAIN_CONF_FILE, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static IProductApplicationContext getInstance() {
        return ContextHolder.context;
    }

    @Override
    public String product() {
        return product;
    }

    @Override
    public String app() {
        return app;
    }

    @Override
    public String appName() {
        return appName;
    }

    @Override
    public String zkIp() {
        return zkIp;
    }

    @Override
    public String zkPort() {
        return zkPort;
    }

    @Override
    public String zkConnectString() {
        return zkConnectString;
    }

    @Override
    public String backupDir() {
        return backupDir;
    }

    private static class ContextHolder {
        private static final IProductApplicationContext context = new ProductApplicationContext();
    }
}
