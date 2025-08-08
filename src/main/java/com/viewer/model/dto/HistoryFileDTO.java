package com.viewer.model.dto;

import com.viewer.trivial.enumdata.FileType;
import lombok.Data;

/**
 * @author hcw
 * @date 2025/8/8 16:03:02
 */
@Data
public class HistoryFileDTO {
    private String name;
    private long size;
    private FileType type;
    private String filePath;
}
