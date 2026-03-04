package com.viewer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author hcw
 * @date 2025/8/11 16:48:53
 */
@Data
@Configuration
@ConfigurationProperties()
public class FileConfig {
    private String tempFileDir;//临时文件夹，存放下载源文件和转换临时文件
}
