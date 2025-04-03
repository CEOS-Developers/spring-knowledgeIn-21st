package com.ceos21.knowledgein.global.dto;

import lombok.Data;

@Data
public class ResponseError {
    private String path;
    private String messageDetail;
    private String errorDetail;
}
