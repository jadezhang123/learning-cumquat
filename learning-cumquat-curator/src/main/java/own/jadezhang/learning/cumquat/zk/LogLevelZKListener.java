package own.jadezhang.learning.cumquat.zk;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import own.jadezhang.learning.cumquat.zk.listener.AbstractNodeListener;

/**
 * Created by Zhang Junwei on 2017/4/24 0024.
 */
public class LogLevelZKListener extends AbstractNodeListener {
    private final static Logger logger = LoggerFactory.getLogger(LogLevelZKListener.class);

    @Override
    public void onChanged(NodeCache cache) {
        byte[] data = cache.getCurrentData().getData();
        logger.info("zk node [{}] has been changed, the new value is: {}", path, data);
    }

}
