package own.jadezhang.learning.cumquat.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zhang Junwei on 2017/7/20.
 */
public class ExampleClientThatLocks {
    private final InterProcessMutex lock;
    private final FakeLimitedResource resource;
    private final String clientName;

    public ExampleClientThatLocks(CuratorFramework client, String lockPath, FakeLimitedResource resource, String clientName) {
        this.resource = resource;
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " 不能得到互斥锁");
        }

        try {
            System.out.println(clientName + " 已获取到互斥锁");
            resource.use(); // 使用资源
            Thread.sleep(5000);
        } finally {
            System.out.println(clientName + " 释放互斥锁");
            lock.release(); // 总是在finally中释放
        }
    }

    public void doWorkReLock(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " 不能得到互斥锁");
        }
        System.out.println(clientName + " 已获取到互斥锁");
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " 不能得到互斥锁");
        }
        lock.acquire();
        System.out.println(clientName + " 再次获取到互斥锁");
        try {
            resource.use(); // 使用资源
            Thread.sleep(1000);
        } finally {
            System.out.println(clientName + " 释放互斥锁");
            lock.release(); // 总是在finally中释放
            lock.release(); // 获取锁几次 释放锁也要几次
        }
    }

}
