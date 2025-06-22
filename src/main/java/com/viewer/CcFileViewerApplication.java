package com.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.viewer.trivial.filter")
public class CcFileViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CcFileViewerApplication.class, args);
    }

}
