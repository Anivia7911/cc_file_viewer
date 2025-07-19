package com.viewer.controller;

import com.viewer.service.FileViewerService;
import com.viewer.trivial.FileViewerConst;
import com.viewer.trivial.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FileViewerController {

    private FileViewerService service;

    @Autowired
    void setService(
            FileViewerService service
    ){
        this.service = service;
    }

//    file:///Users/cwh/测试/测试.pdf
//    http://localhost:7911/preview?filePath=ZmlsZTovLy9Vc2Vycy9jd2gv5rWL6K+VL+a1i+ivlS5wZGY=
    @GetMapping("/preview")
    public String filePreview(HttpServletRequest req, String filePath, Model model) {
        String fileUrl = WebUtils.decodeUrl(filePath);
        if (fileUrl == null || fileUrl.length() == 0) {
            model.addAttribute("errMsg", "filePath: " + filePath);
            return FileViewerConst.ERROR_PAGE;
        }
        return service.filePreview(req, fileUrl, model);
    }

    /**
     * 测试
     */
    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("users", List.of(new User("1", "张三"), new User("2", "李四")));
        return "test";
    }


}
