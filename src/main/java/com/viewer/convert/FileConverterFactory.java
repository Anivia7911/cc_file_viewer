package com.viewer.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: Jie Bugui
 * @create: 2025-06-22 20:52
 */
@Component
public class FileConverterFactory {

    @Autowired
    private Map<String, FileConverter> converterMap;


    public FileConverter getFileConverter(String converterType) {
        if (converterMap == null) {
            return null;
        }
        return converterMap.get(converterType);
    }
}
