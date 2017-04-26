package curator;

import org.junit.Test;
import own.jadezhang.learning.cumquat.zookeeper.curator.ZKClientFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */

public class ZKClientTester {
    @Test
    public void testConnect() throws Exception {

        final int i= 10000000;
        ExecutorService service = Executors.newCachedThreadPool();
        for (int k = 0; k < 5; k ++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < i; j++) {
                        System.out.println(Thread.currentThread().getName()+":"+ ZKClientFactory.getClient().getState());
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        service.shutdown();
        for (int j = 0; j < i; j++) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
