package own.jadezhang.learning.cumquat.dynconfig;

import own.jadezhang.learning.cumquat.zookeeper.DynamicZKAccessor;
import own.jadezhang.learning.cumquat.zookeeper.listener.NodeListener;

/**
 * Created by Zhang Junwei on 2017/4/26 0026.
 */
public class DefaultDynamicConfiguration implements DynamicConfiguration{

    private DynamicZKAccessor zkAccessor;

    public DefaultDynamicConfiguration(DynamicZKAccessor zkAccessor){
        this.zkAccessor = zkAccessor;
    }

    @Override
    public String formatPtah(String group, String dataId) {
        return null;
    }

    @Override
    public String formatPtah(String product, String app, String group, String dataId) {
        return null;
    }

    @Override
    public void setConfig(String path, String value) throws Exception {

    }

    @Override
    public void deleteConfig(String path, String value) throws Exception {

    }

    @Override
    public String getConfigOfCurrApp(String group, String dataId) {
        return null;
    }

    public String getConfig(String product, String app, String group, String dataId) {
        String path = String.format(PATH_FORMATTER, product, app, group, dataId);
        byte[] data = zkAccessor.getData(path);
        return new String(data);
    }

    @Override
    public String registerListener(String path, final DataChangedListener listener) {
        byte[] bytes = zkAccessor.addNodeListener(path, new NodeListener() {
            @Override
            public void onDataChanged(String path) {
                byte[] data = zkAccessor.getData(path);
                if (data == null) {
                    //从本地容灾备份中获取

                }
                listener.process(new String(data));
            }
        });
        return new String(bytes);
    }

}