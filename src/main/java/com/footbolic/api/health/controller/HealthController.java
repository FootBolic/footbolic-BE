package com.footbolic.api.health.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AWS Health 검사를 위한 API")
@RequestMapping("/health")
@RestController
public class HealthController {

    @Operation(summary = "AWS Health 검사 API", description = "AWS Health 검사 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/check")
    public String healthCheck() {
        return "SUCCESS";
    }
}
