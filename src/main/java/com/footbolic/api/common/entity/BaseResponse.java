package com.footbolic.api.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse {

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    private String message;

    private Object data;

}
