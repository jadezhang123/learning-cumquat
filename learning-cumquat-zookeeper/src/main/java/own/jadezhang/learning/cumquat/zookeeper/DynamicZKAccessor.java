package own.jadezhang.learning.cumquat.zookeeper;

import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;
import own.jadezhang.learning.cumquat.zookeeper.listener.StateListener;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public interface DynamicZKAccessor extends ZKAccessor {

    /**
     *
     * @param path
     * @param listener
     * @return
     */
    byte[] addNodeListener(String path, NodeListener listener);

    void removeNodeListener(String path, NodeListener listener);

    void addStateListener(StateListener listener);

    void removeStateListener(StateListener listener);
}
