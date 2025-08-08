package com.viewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hcw
 * @date 2025/8/7 17:54:12
 */

@Controller
public class HomePageController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
