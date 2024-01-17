package com.footbolic.api.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse {

    private boolean success;

    private String message;

    private Object data;

}
