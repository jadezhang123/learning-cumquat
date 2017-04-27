package curator;

import org.junit.Test;
import own.jadezhang.learning.cumquat.zookeeper.curator.ZKClientFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */

public class ZKClientTester {
    @Test
    public void testConnect() throws Exception {

        final int i= 10000000;
        for (int j = 0; j < i; j++) {
            try {
                System.out.println(ZKClientFactory.getClient().getState());
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
