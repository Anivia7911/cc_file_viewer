package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * CAD 文件预览（通过 PDF 展示）
 * @author hcw
 */
@Service
public class CadFileConverter extends AbstractFileConverter {

    @Override
    protected void convertFileHandle(FileAttributeModel model) {
        // 由于 CAD 转换需要专用库，这里预留实现接口
        // 建议集成 Kabeja 将 dwg/dxf 转为 svg，或转为 pdf
        File file = new File(model.getFilePath());
        if (!file.exists()) {
            model.setConvertedFileType(FileType.error);
            return;
        }
        
        // 暂时不支持，可以设置转换结果为 error 页面
        model.setConvertedFileType(FileType.error);
    }
}
