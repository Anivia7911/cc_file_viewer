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

//    http://localhost:7911/preview?filePath=ZmlsZTovLy9DOi9Vc2Vycy93aW4xMC9Eb2N1bWVudHMv4oCc5bGx5Lic6YCa4oCd5bmz5Y+wIOWFqOecgee7n+S4gOeUqOaIt+euoeeQhuezu+e7n+aOpeWFpeinhOiMgzIwMjExMTE2LXYyLjMucGRm
    @GetMapping("/preview")
    public String filePreview(HttpServletRequest req, String filePath, Model model) {
        String fileUrl = WebUtils.decodeUrl(filePath);
        if (fileUrl == null || fileUrl.length() == 0) {
            model.addAttribute("errMsg", "filePath: " + filePath);
            return FileViewerConst.ERROR_PAGE;
        }
        return service.filePreview(req, fileUrl, model);
    }


}
