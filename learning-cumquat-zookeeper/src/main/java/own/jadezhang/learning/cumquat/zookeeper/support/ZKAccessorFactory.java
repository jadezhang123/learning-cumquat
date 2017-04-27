package own.jadezhang.learning.cumquat.zookeeper.support;

import own.jadezhang.learning.cumquat.zookeeper.DynamicZKAccessor;
import own.jadezhang.learning.cumquat.zookeeper.ZKAccessor;

/**
 * Created by Zhang Junwei on 2017/4/27.
 */
public class ZKAccessorFactory {

    public static ZKAccessor zkAccessor(){
        return new DefaultZKAccessor();
    }

    public static DynamicZKAccessor dynamicZKAccessor(){
        return new DefaultDynamicZKAccessor();
    }
}
