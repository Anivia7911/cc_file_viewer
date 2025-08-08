package com.viewer.controller;

import com.viewer.model.vo.ResultVO;
import com.viewer.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hcw
 * @date 2025/8/8 09:55:09
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upload(file);
    }

    @GetMapping("/history-list")
    public ResultVO selectHistoryList(
            @RequestParam("page") int page,
            @RequestParam("rows") int rows
    ) {
        return ResultVO
                .builder()
                .list(fileService.selectHistoryList(page, rows))
                .total(fileService.selectHistoryCount())
                .build();
    }

}
