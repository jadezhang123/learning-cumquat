package own.jadezhang.learning.cumquat.zookeeper.util;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
    public static void backupData(final byte[] content, final String path, final ConcurrentMap<String, Object> recoverDataCache) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //进行数据容灾处理
                    Object oldCacheData = recoverDataCache.putIfAbsent(path, content);
                    //第一次 强制刷盘
                    if (oldCacheData == null) {
                        doBackup(content, path);
                    }
                    //数据有更新也刷盘
                    if (oldCacheData != null && content != oldCacheData) {
                        doBackup(content, path);
                    }
                    logger.warn("backup data of {} successfully", path);
                } catch (Exception e) {
                    logger.error("backing up data of {} occurred error", e);
                }
            }
        });
    }

    private static void doBackup(final byte[] content, final String path) throws IOException {
        String parentDir = System.getProperty("java.io.tmpdir");

        File recoveryFile = new File(parentDir + ZK_TAG + path);
        Files.createParentDirs(recoveryFile);
        Files.write(content, recoveryFile);
    }

    /**
     * 从本地加载备份数据
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] loadRecoverData(String path) throws IOException {
        String parentDir = System.getProperty("java.io.tmpdir");
        File recoveryFile = new File(parentDir + path);

        if (!recoveryFile.exists()) {//创建目录
            Files.createParentDirs(recoveryFile);
        }
        //从本地获取配置数据
        return Files.toByteArray(recoveryFile);
    }

}
