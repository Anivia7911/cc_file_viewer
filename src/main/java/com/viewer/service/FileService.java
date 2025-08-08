package com.viewer.service;

import com.viewer.model.dto.HistoryFileDTO;
import com.viewer.trivial.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public List<HistoryFileDTO> selectHistoryList(int page, int rows) {
        List<HistoryFileDTO> list = new ArrayList<>();
        if (!Files.exists(tempDir)) {
            return list;
        }
        File dir = new File(String.valueOf(tempDir));
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return list;
        }
        
        File[] files = Objects.requireNonNull(dir.listFiles());
        // 按文件最后修改时间倒序排列（最新的在前）
        Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        
        // 计算分页起始位置
        int start = (page - 1) * rows;
        int end = Math.min(start + rows, files.length);
        
        // 如果起始位置超出文件数组范围，则返回空列表
        if (start >= files.length) {
            return list;
        }
        
        // 只处理当前页的文件
        for (int i = start; i < end; i++) {
            File file = files[i];
            HistoryFileDTO dto = new HistoryFileDTO();
            dto.setName(file.getName());
            dto.setSize(file.length());
            dto.setType(FileUtils.typeFromFileName(file.getName()));
            String filePath = "file:///" + tempDir.toString().replace("\\", "/") + "/" + file.getName();
            dto.setFilePath(Base64.getEncoder().encodeToString(filePath.getBytes(StandardCharsets.UTF_8)));
            list.add(dto);
        }
        return list;
    }

    public int selectHistoryCount() {
        if (!Files.exists(tempDir)) {
            return 0;
        }
        File dir = new File(String.valueOf(tempDir));
        if (!dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return 0;
        }
        return Objects.requireNonNull(dir.listFiles()).length;
    }
}