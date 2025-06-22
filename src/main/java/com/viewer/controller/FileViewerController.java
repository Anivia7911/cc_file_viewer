package com.viewer.controller;

import com.viewer.service.FileViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileViewerController {

    private FileViewerService service;

    @Autowired
    void setService(
            FileViewerService service
    ){
        this.service = service;
    }

    /**
     * 获取文件预览地址
     */
    @RequestMapping("/get/preview-url")
    public String getFileViewerUrl(String fileUrl, String converterType, Model model ) {
        return service.getFileViewerUrl(fileUrl, converterType, model);
    }



}
