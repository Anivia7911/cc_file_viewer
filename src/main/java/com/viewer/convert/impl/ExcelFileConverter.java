package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Excel文件转换器
 * @author hcw
 * @date 2025/7/21 13:59:13
 */
@Service
public class ExcelFileConverter extends AbstractFileConverter {
    @Override
    protected void convertFileHandle(FileAttributeModel model) {
//        // 确保输出目录存在
//        File outDir = new File(outputDir);
//        if (!outDir.exists()) {
//            outDir.mkdirs();
//        }
//
//        // 构建命令
//        String command = String.format("soffice --headless --convert-to pdf %s --outdir %s",
//                inputFile.getAbsolutePath(), outputDir);
//
//        Process process = Runtime.getRuntime().exec(command);
//        int exitCode = process.waitFor();
//        if (exitCode == 0) {
//            // 转换成功，生成的文件名是原文件名（不包括扩展名）加上.pdf
//            String fileName = inputFile.getName().replaceFirst("[.][^.]+$", "") + ".pdf";
//            return new File(outputDir, fileName);
//        } else {
//            // 转换失败，读取错误流
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String line;
//            StringBuilder error = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                error.append(line).append("\n");
//            }
//            throw new RuntimeException("转换失败，退出代码: " + exitCode + "\n错误信息：" + error.toString());
//        }
    }

}