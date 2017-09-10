package own.jadezhang.learning.cumquat.test.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import own.jadezhang.learning.cumquat.zookeeper.curator.ZKClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */

public class ZKClientTester {
    public static void testConnect() throws Exception {

        final int i = 10000000;
        for (int j = 0; j < i; j++) {
            try {
                System.out.println(ZKClientFactory.getClient().getState());
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //ZKClientTester.testConnect();

        //默认创建的根节点是没有做权限控制的--需要自己手动加权限
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl;

            @Override
            public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "ScheduleAdmin:password")));
                    this.acl = acl;
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
        String scheme = "digest";
        byte[] auth = "ScheduleAdmin:password".getBytes();
        int connectionTimeoutMs = 5000;
        String connectString = "192.168.230.128:2181";
        CuratorFramework client = CuratorFrameworkFactory.builder().aclProvider(aclProvider).
                authorization(scheme, auth).
                connectionTimeoutMs(connectionTimeoutMs).
                connectString(connectString).
                retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
        client.start();
        String path = "/configs/qky/qky_classeval/common/JSTORM_CONF";
        client.create().creatingParentsIfNeeded().forPath(path);
    }
}
