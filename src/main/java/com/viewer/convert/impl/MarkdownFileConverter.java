package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Markdown 文件预览（转换为 HTML 预览）
 * @author hcw
 */
@Service
public class MarkdownFileConverter extends AbstractFileConverter {

    @Override
    protected void convertFileHandle(FileAttributeModel model) {
        try {
            File file = new File(model.getFilePath());
            if (!file.exists()) {
                model.setConvertedFileType(FileType.error);
                return;
            }

            // 读取文件内容
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // 配置 flexmark 选项
            MutableDataSet options = new MutableDataSet();
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();

            // 解析并转换
            String html = renderer.render(parser.parse(content));

            // 将 HTML 字符串直接存入 resultData
            model.setResultData(html);
            model.setConvertedFileType(FileType.md);
            model.setConvertedFilePath(model.getFileUrl());

        } catch (Exception e) {
            e.printStackTrace();
            model.setConvertedFileType(FileType.error);
        }
    }
}
