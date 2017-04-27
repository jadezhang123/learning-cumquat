package own.jadezhang.learning.cumquat.dynconfig;

import own.jadezhang.learning.cumquat.zookeeper.support.ZKAccessorFactory;

/**
 * Created by Zhang Junwei on 2017/4/27 0027.
 */
public class DynamicConfigurationFactory {
    public static DynamicConfiguration dynamicConfiguration(){
        return new DefaultDynamicConfiguration(ZKAccessorFactory.dynamicZKAccessor());
    }
}
