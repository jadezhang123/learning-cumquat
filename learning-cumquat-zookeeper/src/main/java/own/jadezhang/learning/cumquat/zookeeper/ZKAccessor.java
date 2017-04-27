package own.jadezhang.learning.cumquat.zookeeper;

import java.util.List;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public interface ZKAccessor {

    void create(String path, boolean ephemeral);

    void delete(String path);

    byte[] getData(String path);

    List<String> getChildren(String path);

    boolean isConnected();

    void close();
}
