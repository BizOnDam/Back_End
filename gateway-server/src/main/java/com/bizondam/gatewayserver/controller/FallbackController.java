package com.bizondam.gatewayserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

  @RequestMapping("/fallback/company")
  public ResponseEntity<String> companyFallback() {
    return ResponseEntity.ok("Company Service is temporarily unavailable. Please try again later.");
  }

  @RequestMapping("/fallback/matching")
  public ResponseEntity<String> matchingFallback() {
    return ResponseEntity.ok("Matching Service is temporarily unavailable. Please try again later.");
  }
}