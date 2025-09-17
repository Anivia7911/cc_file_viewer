package com.viewer.trivial.scheduler;

import com.viewer.config.FileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Properties;


/**
 * @author hcw
 * @date 2025/8/11 16:43:53
 */
@Slf4j
@Component
@EnableScheduling
public class ConfigRefreshScheduler {

    private ConfigurableEnvironment environment;

    @Autowired
    private FileConfig fileConfig;


    @Scheduled(cron = "*/1 * * * * ?")
    public void refreshConfig() {
        try {
            // 重新加载配置文件
            Resource resource = new ClassPathResource("application.properties");
            if (resource.exists()) {
                Properties props = PropertiesLoaderUtils.loadProperties(resource);

                // 更新环境中的属性
                props.forEach((key, value) -> {
                    environment.getPropertySources()
                            .forEach(propertySource -> {
                                // 这里可以更新属性源
                            });
                });

                log.debug("配置刷新成功: {}", fileConfig.getTempFileDir());
            }
        } catch (Exception e) {
            log.error("刷新配置时发生错误", e);
        }
    }
}
