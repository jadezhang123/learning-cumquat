package own.jadezhang.learning.cumquat.zookeeper.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import own.jadezhang.learning.cumquat.zookeeper.ZookeeperAccessor;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public abstract class AbstractZookeeperAccessor implements ZookeeperAccessor {
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

    protected abstract void doClose();

    protected abstract void createPersistent(String path);

    protected abstract void createEphemeral(String path);
}
