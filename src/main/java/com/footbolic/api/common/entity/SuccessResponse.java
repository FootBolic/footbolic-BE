package com.footbolic.api.common.entity;

public class SuccessResponse extends BaseResponse {
    public SuccessResponse(Object data) {
        super(true, "", data);
    }
}
