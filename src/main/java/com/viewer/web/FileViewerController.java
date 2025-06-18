package com.viewer.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class FileViewerController {

    /**
     * 获取文件预览地址
     */
    @GetMapping("/get/preview-url")
    public String getFileViewerUrl(String fileUrl ) {
        return null;
    }



}
