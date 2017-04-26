package own.jadezhang.learning.cumquat.zookeeper.support;

import own.jadezhang.learning.cumquat.zookeeper.DynamicZookeeperAccessor;
import own.jadezhang.learning.cumquat.zookeeper.ZookeeperAccessor;

/**
 * Created by Zhang Junwei on 2017/4/27.
 */
public class ZookeeperAccessorFactory {

    public static ZookeeperAccessor zookeeperAccessor(){
        return new DefaultZookeeperAccessor();
    }

    public static DynamicZookeeperAccessor dynamicZookeeperAccessor(){
        return new DefaultDynamicZookeeperAccessor();
    }
}
