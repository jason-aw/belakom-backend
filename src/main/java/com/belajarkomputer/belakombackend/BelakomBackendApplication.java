package com.belajarkomputer.belakombackend;

import com.belajarkomputer.belakombackend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BelakomBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BelakomBackendApplication.class, args);
  }

}
