package own.jadezhang.learning.cumquat.zookeeper.support;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import own.jadezhang.learning.cumquat.zookeeper.ZKAccessor;
import own.jadezhang.learning.cumquat.zookeeper.curator.ZKClientFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Zhang Junwei on 2017/4/27.
 */
public class DefaultZKAccessor implements ZKAccessor {
    protected static final Logger logger = LoggerFactory.getLogger(DefaultZKAccessor.class);
    protected CuratorFramework zkClient;
    private volatile boolean closed = false;

    public DefaultZKAccessor() {
        zkClient = ZKClientFactory.getClient();
    }

    @Override
    public void create(String path, boolean ephemeral) {
        int i = path.lastIndexOf('/');
        if (i > 0) {
            create(path.substring(0, i), false);
        }
        if (ephemeral) {
            createEphemeral(path);
        } else {
            createPersistent(path);
        }
    }

    @Override
    public void create(String path, String data, boolean ephemeral) {
        create(path, ephemeral);
        try {
            zkClient.setData().forPath(path, data.getBytes("utf-8"));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            zkClient.delete().forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData(String path) {
        try {
            return zkClient.getData().forPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return zkClient.getChildren().forPath(path);
        } catch (NoNodeException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isConnected() {
        return zkClient.getZookeeperClient().isConnected();
    }

    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            doClose();
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        }
    }

    private void doClose() {
        zkClient.close();
    }

    private void createPersistent(String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().forPath(path);
        } catch (NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createEphemeral(String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
