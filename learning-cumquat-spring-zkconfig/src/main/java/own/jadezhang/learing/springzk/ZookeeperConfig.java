package own.jadezhang.learing.springzk;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * Created by Zhang Junwei on 2017/4/28.
 */
public class ZookeeperConfig extends PropertySourcesPlaceholderConfigurer {
    private ZookeeperResource zkLocation;
    @Override
    public void setLocation(Resource location) {
        super.setLocation(location);
        zkLocation = (ZookeeperResource) location;
    }
}
