package own.jadezhang.learing.springzk;

import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.AbstractResource;
import own.jadezhang.learning.cumquat.zookeeper.curator.ZKClientFactory;
import own.jadezhang.learning.cumquat.zookeeper.util.ZKBackupUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Zhang Junwei on 2017/4/27.
 */
public class ZookeeperResource extends AbstractResource implements ApplicationContextAware {

    public static final String URL_HEADER = "zk://";
    //启动配置路径
    private static final String PATH_FORMATTER = "/startConfigs/%s/%s/configs";

    private String path = String.format(PATH_FORMATTER, "learning", "cumquat");

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperResource.class);

    private ConcurrentMap<String, Object> recoverDataCache = Maps.newConcurrentMap();

    private ApplicationContext ctx;

    public InputStream getInputStream() throws IOException {
        byte[] data = null;
        try {
            CuratorFramework client = ZKClientFactory.getClient();
            if (client.checkExists().forPath(path) != null) {
                data = client.getData().forPath(path);
            } else {
                logger.error("{} do not exists at zookeeper[{}]", path, client.getZookeeperClient().getCurrentConnectionString());
                System.exit(-1);
            }
        } catch (Exception e) {
            logger.error("zookeeper server occurred error", e);
            //从本地容灾缓存中获取配置数据
            data = ZKBackupUtil.loadRecoverData(path);
        }
        //备份配置数据到本地
        ZKBackupUtil.backupData(data, path, recoverDataCache);

        return new ByteArrayInputStream(data);
    }

    @Override
    public boolean exists() {
        try {
            return null != ZKClientFactory.getClient().checkExists().forPath(path);
        } catch (Exception e) {
            logger.error("Failed  to detect the config in zookeeper.", e);
            return false;
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        return new URL(URL_HEADER + path);
    }

    @Override
    public String getFilename() throws IllegalStateException {
        return path;
    }

    public String getDescription() {
        return "start configs at " + URL_HEADER + path;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

}
