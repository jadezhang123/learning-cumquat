package own.jadezhang.learning.cumquat.zookeeper.listener;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public interface StateListener {
    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void onDisconnected();

    void onConnected();

    void onReconnected();


}
