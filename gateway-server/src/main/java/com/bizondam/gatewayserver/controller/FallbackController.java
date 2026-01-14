package com.bizondam.gatewayserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FallbackController {

  @RequestMapping("/fallback/company")
  public ResponseEntity<String> companyFallback() {
    return ResponseEntity.ok("Company Service is temporarily unavailable. Please try again later.");
  }

  @RequestMapping("/fallback/matching")
  public ResponseEntity<Map<String, Object>> matchingFallback() {
    Map<String, Object> body = Map.of(
            "success", false,
            "code", 503,
            "message", "Matching Service is temporarily unavailable. Please try again later.",
            "data", null
    );
    return ResponseEntity.status(503).body(body);
  }
}