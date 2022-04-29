package com.belajarkomputer.belakombackend;

import com.belajarkomputer.belakombackend.config.AppProperties;
import com.belajarkomputer.belakombackend.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.Resource;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BelakomBackendApplication implements CommandLineRunner {

  @Resource
  FileStorageService fileStorageService;

  public static void main(String[] args) {
    SpringApplication.run(BelakomBackendApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    fileStorageService.init();
  }
}
