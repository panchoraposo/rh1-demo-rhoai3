package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @RestController
  static class HealthController {
    @GetMapping("/health")
    public java.util.Map<String, String> health() {
      return java.util.Map.of("status", "ok");
    }
  }
}

