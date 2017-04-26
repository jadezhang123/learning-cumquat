package own.jadezhang.learning.cumquat.zookeeper.support;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import own.jadezhang.learning.cumquat.zookeeper.DynamicZookeeperAccessor;
import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;
import own.jadezhang.learning.cumquat.zookeeper.listener.StateListener;

import java.util.concurrent.Executors;

/**
 * Created by Zhang Junwei on 2017/4/26.
 */
public abstract class AbstractDynamicZookeeperAccessor extends DefaultZookeeperAccessor implements DynamicZookeeperAccessor {

    @Override
    public byte[] addNodeListener(String path, NodeListener listener) {
        try {
            return zkClient.getData().usingWatcher(new CuratorWatcherImpl(listener)).forPath(path);
        } catch (NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void removeNodeListener(String path, NodeListener listener) {
        ((CuratorWatcherImpl) listener).unwatch();
    }

    @Override
    public void addStateListener(StateListener listener) {

    }

    @Override
    public void removeStateListener(StateListener listener) {

    }

    private class CuratorWatcherImpl implements CuratorWatcher {

        private volatile NodeListener listener;

        public CuratorWatcherImpl(NodeListener listener) {
            this.listener = listener;
        }

        public void unwatch() {
            this.listener = null;
        }

        public void process(WatchedEvent event) throws Exception {
            if (listener != null) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        //
                    }
                });
            }
        }
    }
}
