package own.jadezhang.learning.cumquat.dynconfig;

import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import own.jadezhang.learning.cumquat.zookeeper.util.ZKBackupUtil;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

/**
 * Created by Zhang Junwei on 2017/4/27.
 */
public class CuratorDynamicConfiguration implements DynamicConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CuratorDynamicConfiguration.class);

    private CuratorFramework zkClient;

    private ConcurrentMap<String, byte[]> recoverDataCache = Maps.newConcurrentMap();

    public CuratorDynamicConfiguration(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public String formatPtah(String group, String dataId) {
        return null;
    }

    @Override
    public String formatPtah(String product, String app, String group, String dataId) {
        return String.format(PATH_FORMATTER, product, app, group, dataId);
    }

    @Override
    public void setConfig(String path, String value) {
        try {
            if (zkClient.checkExists().forPath(path) == null) {
                createConfig(path, value);
            } else {
                zkClient.setData().forPath(path, value.getBytes("utf-8"));
            }
        } catch (Exception e) {
            logger.error("setting config fro {} occurred error:{}", path, e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void createConfig(String path, String value) {
        try {
            zkClient.create().creatingParentsIfNeeded().forPath(path, value.getBytes("utf-8"));
        } catch (Exception e) {
            logger.error("creating config fro {} occurred error:{}", path, e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteConfig(String path) {
        try {
            zkClient.delete().forPath(path);
        } catch (Exception e) {
            logger.error("deleting config fro {} occurred error:{}", path, e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String getConfigOfCurrApp(String group, String dataId) {
        return getData(formatPtah(group, dataId));
    }

    @Override
    public String getConfig(String product, String app, String group, String dataId) {
        return getData(formatPtah(product, app, group, dataId));
    }

    @Override
    public String registerListener(final String path, final DataChangedListener listener) {
        try {
            byte[] bytes = zkClient.getData().usingWatcher(new CuratorWatcher() {
                @Override
                public void process(final WatchedEvent event) throws Exception {
                    //数据改变了触发监听器
                    if (Watcher.Event.EventType.NodeDataChanged == event.getType() && listener != null) {
                        //开启新线程处理
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                String data = getData(path);
                                if (data != null) {
                                    listener.process(data);
                                }
                            }
                        });
                    }
                }
            }).forPath(path);
            return new String(bytes);
        } catch (Exception e) {
            logger.warn("registering listener for {} occurred error : {}", path, e.getMessage());
        }
        return null;
    }

    /**
     * 获取数据，先从zookeeper获取数据并备份到本地，若失败则从本地获取
     * @param path
     * @return
     */
    private String getData(String path) {
        try {
            try {
                byte[] content = zkClient.getData().forPath(path);
                //备份数据
                ZKBackupUtil.backupData(content, path, recoverDataCache);
                return new String(content);
            } catch (Exception e) {
                //从本地容灾备份中获取数据
                return new String(ZKBackupUtil.loadBackupData(path));
            }
        } catch (IOException e) {
            logger.warn("FAILED to load backup data from local", e.getMessage());
            return null;
        }
    }

}
