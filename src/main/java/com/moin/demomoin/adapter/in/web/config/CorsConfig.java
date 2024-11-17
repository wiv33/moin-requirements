package com.moin.demomoin.adapter.in.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;

@Configuration
public class CorsConfig {

  @Bean
  public CorsRegistry corsRegistry() {
    var corsRegistry = new CorsRegistry();
    corsRegistry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
    return corsRegistry;
  }
}
