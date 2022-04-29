package com.belajarkomputer.belakombackend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
  void init();
  Path save(MultipartFile file);
  Resource load(String filename);
  void deleteAll();
  Stream<Path> loadAll();
  boolean delete(String filename);
}
