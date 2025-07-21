package com.viewer.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Configuration
public class TempFileCleanupOnStartup {

    @PostConstruct
    public void cleanupTempDirectory() {
        try {
            // 获取项目根目录下的 temp 目录
            Path tempDir = Paths.get("file/temp");

            if (Files.exists(tempDir) && Files.isDirectory(tempDir)) {
                // 遍历并删除所有文件（不删除目录本身）
                Files.walk(tempDir)
                        .sorted(Comparator.reverseOrder()) // 保证先删除文件，再删除子目录
                        .forEach(path -> {
                            try {
                                if (!path.equals(tempDir)) {
                                    Files.delete(path);
                                    System.out.println("已清除临时文件: " + path);
                                }
                            } catch (IOException e) {
                                System.err.println("无法删除文件: " + path + "，原因: " + e.getMessage());
                            }
                        });
            } else {
                System.out.println("临时目录不存在或不是一个目录: " + tempDir.toAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("启动时清理临时目录失败");
            e.printStackTrace();
        }
    }
}
