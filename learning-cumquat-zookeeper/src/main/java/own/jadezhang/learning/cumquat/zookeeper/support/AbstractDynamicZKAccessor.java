package own.jadezhang.learning.cumquat.zookeeper.support;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.Pathable;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import own.jadezhang.learning.cumquat.zookeeper.DynamicZKAccessor;
import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;
import own.jadezhang.learning.cumquat.zookeeper.listener.StateListener;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Zhang Junwei on 2017/4/26.
 */
public abstract class AbstractDynamicZKAccessor extends DefaultZKAccessor implements DynamicZKAccessor {

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

        public void process(final WatchedEvent event) throws Exception {
            if (listener != null) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        switch (event.getType()){
                            case NodeCreated:
                                break;
                            case NodeDataChanged:
                                listener.onDataChanged(event.getPath());
                                break;
                            case NodeDeleted:
                                break;
                            case NodeChildrenChanged:
                                break;
                        }
                    }
                });
            }
        }
    }
}
