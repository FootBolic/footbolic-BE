package com.footbolic.api.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Swagger 사용 예제", description = "Swagger 사용법 예시용 Controller")
@RestController
public class testController {

    @Operation(summary = "파라미터 반환", description = "파라미터로 받은 내용 그대로 반환")
    @Parameter(name = "param1", description = "파라미터 문자열", required = true)
    @Parameter(name = "param2", description = "파라미터 숫자")
    @GetMapping(value = "/test")
    public ResponseEntity<String> test(
            @RequestParam(name = "param1") String param1,
            @RequestParam(name = "param2", required = false) int param2
    ) {
        System.out.println(">>>>>>>>>>>>>>this is testController");
        return ResponseEntity.ok("param is " + param1 + " and " + (param2+1));
    }
}
