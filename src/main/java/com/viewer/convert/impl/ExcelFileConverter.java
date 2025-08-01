package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
        //todo
    }

}