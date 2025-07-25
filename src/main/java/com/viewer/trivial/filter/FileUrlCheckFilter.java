package com.viewer.trivial.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @author: Jie Bugui
 * @create: 2025-06-22 23:29
 */
@WebFilter(urlPatterns = "/get/preview-url")
public class FileUrlCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String fileUrl = request.getParameter("fileName");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
