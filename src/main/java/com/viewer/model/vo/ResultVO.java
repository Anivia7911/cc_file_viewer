package com.viewer.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * @author hcw
 * @date 2025/8/8 15:52:48
 */
@Builder
@Data
public class ResultVO {
    private int success;
    private String message;
    private Collection<?> list;
    private int total;
}
