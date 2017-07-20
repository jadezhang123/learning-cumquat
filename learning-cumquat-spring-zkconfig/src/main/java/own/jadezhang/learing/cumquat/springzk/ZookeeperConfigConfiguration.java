package own.jadezhang.learing.cumquat.springzk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Zhang Junwei on 2017/4/28.
 */
@Configuration
public class ZookeeperConfigConfiguration {

    @Bean
    public ZookeeperConfig zookeeperConfig() {
        ZookeeperConfig zookeeperConfig = new ZookeeperConfig();
        zookeeperConfig.setLocation(new ZookeeperResource());
        return zookeeperConfig;
    }
}
