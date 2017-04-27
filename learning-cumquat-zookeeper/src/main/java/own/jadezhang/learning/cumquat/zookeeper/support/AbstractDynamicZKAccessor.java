package own.jadezhang.learning.cumquat.zookeeper.support;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import own.jadezhang.learning.cumquat.zookeeper.DynamicZKAccessor;
import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;
import own.jadezhang.learning.cumquat.zookeeper.listener.StateListener;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;

/**
 * Created by Zhang Junwei on 2017/4/26.
 */
public abstract class AbstractDynamicZKAccessor extends DefaultZKAccessor implements DynamicZKAccessor {

    private final static Logger logger = LoggerFactory.getLogger(AbstractDynamicZKAccessor.class);

    private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

    @Override
    public byte[] addNodeListener(String path, NodeListener listener) {
        try {
            return zkClient.getData().usingWatcher(new CuratorWatcherImpl(listener)).forPath(path);
        } catch (NoNodeException e) {
            logger.warn("to addNodeListener for {} occurred a error because of no node", path);
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
        stateListeners.add(listener);
    }

    @Override
    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    public Set<StateListener> getSessionListeners() {
        return stateListeners;
    }

    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
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
