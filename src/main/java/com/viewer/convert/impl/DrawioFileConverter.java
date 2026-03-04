package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Draw.io 文件转换器（透传内容由前端渲染）
 * @author hcw
 */
@Service
public class DrawioFileConverter extends AbstractFileConverter {

    @Override
    protected void convertFileHandle(FileAttributeModel model) {
        File file = new File(model.getFilePath());
        if (!file.exists()) {
            model.setConvertedFileType(FileType.error);
            return;
        }

        // Draw.io 预览通常由前端渲染，这里我们设置好状态
        // 传递文件本身的访问 URL 或内容给前端
        model.setConvertedFilePath(model.getFileUrl());
        model.setConvertedFileType(FileType.drawio);
    }
}
