package dynconfig;

import org.junit.Test;
import own.jadezhang.learning.cumquat.dynconfig.DataChangedListener;
import own.jadezhang.learning.cumquat.dynconfig.DynamicConfiguration;
import own.jadezhang.learning.cumquat.dynconfig.DynamicConfigurationFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zhang Junwei on 2017/4/27 0027.
 */
public class DynamicConfigTester {
    public static final String path = "/configs/learning/cumquat/test";

    @Test
    public void name() throws Exception {

        DynamicConfiguration dynamicConfiguration = DynamicConfigurationFactory.dynamicConfiguration();
        dynamicConfiguration.setConfig(path, "hello");
        dynamicConfiguration.registerListener(path, new DataChangedListener() {
            @Override
            public void process(String data) {
                System.out.println("data of " + path + " changed to " + data);
            }
        });

        //延迟测试线程停止
        int i = 10000000;
        for (int j = 0; j < i; j++) {
            TimeUnit.MILLISECONDS.sleep(1000);
        }

    }
}
