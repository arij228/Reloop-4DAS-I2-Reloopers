package com.reloop.transactionservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "0. Test Controller", description = "Endpoints for verifying the microservice is running")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "Hello Test", description = "Returns a simple success message to prove the API is alive.")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello Transaction Service is working perfectly! 🚀");
    }
}