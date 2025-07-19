package com.viewer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String tempDir = System.getProperty("java.io.tmpdir") + File.separator + "pdfviewer";
        registry.addResourceHandler("/pdfviewer/**")
                .addResourceLocations("file:" + tempDir + File.separator);
    }
}
