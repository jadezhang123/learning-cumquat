package own.jadezhang.learning.cumquat.dynconfig;

import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;

/**
 * Created by Zhang Junwei on 2017/4/27 0027.
 */
public interface DynamicConfiguration {

    //zkNode路径： /configs/{product}/{app}/{group}/{dataId}
    String PATH_FORMATTER = "/configs/%s/%s/%s/%s";

    String formatPtah(String group, String dataId);

    String formatPtah(String product, String app, String group, String dataId);

    void setConfig(String path , String value) throws Exception;

    void deleteConfig(String path , String value) throws Exception;

    String getConfigOfCurrApp(String group, String dataId);

    String getConfig(String product, String app, String group, String dataId);

    String registerListener(String path, DataChangedListener listener);
}
