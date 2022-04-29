package com.belajarkomputer.belakombackend.service.impl;

import com.belajarkomputer.belakombackend.service.FileStorageService;
import com.belajarkomputer.belakombackend.utils.FileHelper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {
  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      if (!root.toFile().exists()) {
        Files.createDirectory(root);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public Path save(MultipartFile file) {
    try {
      String generatedFilename =
          FileHelper.generateFilename(Objects.requireNonNull(file.getOriginalFilename()));
      Path path = this.root.resolve(generatedFilename);
      Files.copy(file.getInputStream(), path);
      return path;
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1)
          .filter(path -> !path.equals(this.root))
          .map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

  @Override
  public boolean delete(String filename) {
    Path path = this.root.resolve(filename);
    return path.toFile().delete();
  }
}
