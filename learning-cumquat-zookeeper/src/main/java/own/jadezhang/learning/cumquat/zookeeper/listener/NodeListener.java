package own.jadezhang.learning.cumquat.zookeeper.listener;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public interface NodeListener {
    void onDataChanged(String path);
}
