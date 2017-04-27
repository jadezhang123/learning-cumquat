package own.jadezhang.learning.cumquat.dynconfig;

/**
 * Created by Zhang Junwei on 2017/4/27 0027.
 */
public interface DynamicConfiguration {

    //zkNode路径： /configs/{product}/{app}/{group}/{dataId}
    String PATH_FORMATTER = "/configs/%s/%s/%s/%s";

    String formatPtah(String group, String dataId);

    String formatPtah(String product, String app, String group, String dataId);

    void setConfig(String path, String value);

    void createConfig(String path, String value);

    void deleteConfig(String path);

    String getConfigOfCurrApp(String group, String dataId);

    /**
     * 获取指定项的节点数据
     * @param product
     * @param app
     * @param group
     * @param dataId
     * @return 获取失败将返回null
     */
    String getConfig(String product, String app, String group, String dataId);

    /**
     * 注册数据监听器，只有当path的数据发生改变时才触发
     * @param path
     * @param listener
     * @return 当前path的数据，注册失败将返回null
     */
    String registerListener(String path, DataChangedListener listener);
}
