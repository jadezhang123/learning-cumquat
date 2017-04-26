package own.jadezhang.learning.cumquat.zookeeper.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import own.jadezhang.learning.cumquat.zookeeper.DynamicZookeeperAccessor;
import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;
import own.jadezhang.learning.cumquat.zookeeper.listener.StateListener;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public abstract class AbstractZookeeperAccessor implements DynamicZookeeperAccessor {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractZookeeperAccessor.class);
    private volatile boolean closed = false;

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


    @Override
    public void addNodeListener(NodeListener listener) {

    }

    @Override
    public void removeNodeListener(NodeListener listener) {

    }

    @Override
    public void addStateListener(StateListener listener) {

    }

    @Override
    public void removeStateListener(StateListener listener) {

    }

    protected abstract void doClose();

    protected abstract void createPersistent(String path);

    protected abstract void createEphemeral(String path);
}
