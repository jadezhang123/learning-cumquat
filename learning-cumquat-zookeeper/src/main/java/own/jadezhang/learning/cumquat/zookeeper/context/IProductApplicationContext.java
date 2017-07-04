package own.jadezhang.learning.cumquat.zookeeper.context;

/**
 * Created by Zhang Junwei on 2017/7/4.
 */
public interface IProductApplicationContext {

    String product();

    String app();

    String appName();

    String zkIp();

    String zkPort();

    String zkConnectString();

    String backupDir();
}
