package own.jadezhang.learning.cumquat.zk.listener;

import org.apache.curator.framework.CuratorFramework;

/**
 * Created by Zhang Junwei on 2017/4/24 0024.
 */
public interface IZKListener {

    void executor(CuratorFramework client);

}
