package own.jadezhang.learning.cumquat.zk.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zhang Junwei on 2017/4/24 0024.
 */
public abstract class AbstractNodeListener implements IZKListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractNodeListener.class);

    /**
     * 监听节点的路径
     */
    protected String path;

    @Override
    public void executor(CuratorFramework client) {
        final NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                logger.info("zk node [{}] has been changed", path);
                onChanged(cache);
            }
        });
        try {
            cache.start(true);
        } catch (Exception e) {
            logger.error("Start NodeCache error for path: {}, error info: {}", path, e.getMessage());
        }
    }

    /**
     * 当path代表的节点改变或者被删除此方法会被触发
     * @param cache
     */
    public abstract void onChanged(final NodeCache cache);

    public void setPath(String path) {
        this.path = path;
    }
}
