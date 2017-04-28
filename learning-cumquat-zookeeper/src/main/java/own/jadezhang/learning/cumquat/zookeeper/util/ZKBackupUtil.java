package own.jadezhang.learning.cumquat.zookeeper.util;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

/**
 * zookeeper 数据本地备份工具，用于容灾处理
 */
public class ZKBackupUtil {
    public static final Logger logger = LoggerFactory.getLogger(ZKBackupUtil.class);

    public static final String ZK_TAG = "zk";

    /**
     * 异步进行配置数据本地容灾处理
     * @param content
     * @param path
     * @param recoverDataCache
     */
    public static void backupData(final byte[] content, final String path, final ConcurrentMap<String, byte[]> recoverDataCache) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //进行数据容灾处理
                    byte[] oldCacheData = recoverDataCache.putIfAbsent(path, content);
                    //第一次强制刷盘
                    if (oldCacheData == null) {
                        doBackup(content, path);
                    }
                    //数据有更新也刷盘
                    if (oldCacheData != null && Arrays.equals(content, oldCacheData)) {
                       doBackup(content, path);
                    }
                } catch (Exception e) {
                    logger.error("backing up data of {} occurred error", e);
                }
            }
        });
    }

    /**
     * 将数组复制到备份文件中
     * @param content
     * @param path
     * @throws IOException
     */
    private static void doBackup(byte[] content, String path) throws IOException {
        Files.write(content, getBackupFile(path));
        logger.warn("backup data of {} successfully", path);
    }

    /**
     * 从本地加载备份数据
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] loadRecoverData(String path) throws IOException {
        //从本地获取配置数据
        logger.warn("load recover data from {} at local backup file", path);
        return Files.toByteArray(getBackupFile(path));
    }

    /**
     * 根据path获取备份文件
     * @param path
     * @return
     * @throws IOException
     */
    private static File getBackupFile(String path) throws IOException {
        String parentDir = System.getProperty("java.io.tmpdir");
        File recoveryFile = new File(parentDir + ZK_TAG + File.separator + path);

        if (!recoveryFile.exists()) {
            Files.createParentDirs(recoveryFile);
        }
        return recoveryFile;
    }

    public static void main(String[] args) throws IOException {

        byte[] bytes = loadRecoverData("/startConfigs/learning/cumquat/config");

        System.out.println(new String(bytes));

    }

}
