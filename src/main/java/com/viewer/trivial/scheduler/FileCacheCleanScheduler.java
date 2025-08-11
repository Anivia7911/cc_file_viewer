package com.viewer.trivial.scheduler;

import com.viewer.trivial.cache.FileConvertCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hcw
 * @date 2025/8/11 11:01:47
 */
@Slf4j
@Component
@ConditionalOnExpression("'${file.cache.clean.enabled:false}'.equals('true')")
public class FileCacheCleanScheduler {

    private FileConvertCache fileConvertCache;
    private final Path tempDir = Paths.get("file/temp");

    @Autowired
    void setService(FileConvertCache fileConvertCache) {
        this.fileConvertCache = fileConvertCache;
    }

    //默认每晚3点执行一次
    @Scheduled(cron = "${file.cache.clean.cron:0 0 3 * * ?}")
    public void clean() {
        log.info("file cache clean start");
        //临时文件清理
        try {
            // 删除缓存
            fileConvertCache.invalidateAll();
            if (!Files.exists(tempDir) || !Files.isDirectory(tempDir)) {
                return;
            }
            Files.delete(tempDir);
        } catch (Exception e) {
            log.error("file cache clean fail:{}", e.getMessage());
        }
        log.info("file cache clean end");
    }
}
