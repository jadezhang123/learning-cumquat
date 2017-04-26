package own.jadezhang.learning.cumquat.zookeeper.support;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import own.jadezhang.learning.cumquat.zookeeper.curator.ZKClientFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Zhang Junwei on 2017/4/27.
 */
public class DefaultZookeeperAccessor extends AbstractZookeeperAccessor {

    protected CuratorFramework zkClient;

    public DefaultZookeeperAccessor() {
        zkClient = ZKClientFactory.getClient();
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

    @Override
    protected void doClose() {
        zkClient.close();
    }

    @Override
    protected void createPersistent(String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().forPath(path);
        } catch (NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected void createEphemeral(String path) {
        try {
            zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
