package com.footbolic.api.common.entity;

public class ErrorResponse extends BaseResponse {
    public ErrorResponse(String message) {
        super(false, message, null);
    }
}
