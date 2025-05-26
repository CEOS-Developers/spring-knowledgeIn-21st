package com.ceos21.spring_boot.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    
    @GetMapping("/")
    public String hello() {
        return "Spring server is running!";
    }
}
