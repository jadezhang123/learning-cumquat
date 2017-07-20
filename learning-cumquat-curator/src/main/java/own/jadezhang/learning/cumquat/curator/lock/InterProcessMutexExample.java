package own.jadezhang.learning.cumquat.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhang Junwei on 2017/7/20.
 */
public class InterProcessMutexExample {
    private static final int QTY = 5;
    private static final int REPETITIONS = QTY * 10;
    private static final String PATH = "/examples/locks";
    private static final String ZK_CONNECT_STR = "192.168.230.128:2181";

    public static void main(String[] args) throws Exception {
        final FakeLimitedResource resource = new FakeLimitedResource();
        final List<CuratorFramework> clientList = new ArrayList<CuratorFramework>();
        for (int i = 0; i < QTY; i++) {
            CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_CONNECT_STR, new ExponentialBackoffRetry(1000, 3));

            client.start();
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {

                }
            });
            clientList.add(client);
        }
        System.out.println("连接初始化完成！");
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < QTY; ++i) {
            final int index = i;
            Callable<Void> task = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    try {
                        final ExampleClientThatLocks example = new ExampleClientThatLocks(clientList.get(index), PATH, resource, "Client " + index);
                        for (int j = 0; j < REPETITIONS; ++j) {
                            example.doWork(10, TimeUnit.SECONDS);
                            //example.doWorkReLock(10, TimeUnit.SECONDS);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        CloseableUtils.closeQuietly(clientList.get(index));
                    }
                    return null;
                }
            };
            service.submit(task);
        }
        service.shutdown();
        service.awaitTermination(3, TimeUnit.MINUTES);
        System.out.println("OK!");
    }
}
