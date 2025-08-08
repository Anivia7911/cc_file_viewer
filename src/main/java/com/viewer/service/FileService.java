package com.viewer.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class FileService {

    private final Path tempDir = Paths.get(System.getProperty("user.dir")).resolve("file/local/");

    /**
     * 上传文件并返回地址
     */
    public String upload(MultipartFile file) throws IOException {
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
        //todo... 文件校验

        String fileName = file.getOriginalFilename();
        try (InputStream in = file.getInputStream(); OutputStream out = Files.newOutputStream(Paths.get(tempDir + "/" + fileName))) {
            StreamUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回可以被预览接口识别的格式
        String filePath = "file:///" + tempDir.toString().replace("\\", "/") + "/" + fileName;
        return Base64.getEncoder().encodeToString(filePath.getBytes(StandardCharsets.UTF_8));
    }
}