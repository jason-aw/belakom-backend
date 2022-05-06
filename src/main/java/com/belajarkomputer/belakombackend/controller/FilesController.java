package com.belajarkomputer.belakombackend.controller;

import com.belajarkomputer.belakombackend.model.entity.FileInfo;
import com.belajarkomputer.belakombackend.model.response.FilesResponse;
import com.belajarkomputer.belakombackend.service.FileStorageService;
import com.belajarkomputer.belakombackend.utils.FileHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/files")
@Slf4j
public class FilesController {

  private FileStorageService fileStorageService;

  @PostMapping(value = "/upload")
  public ResponseEntity<FilesResponse> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      Path path = this.fileStorageService.save(file);
      String filename = path.getFileName().toString();
      String generatedUrl = FileHelper.generateUrl(path.getFileName().toString());
      log.info("file uploaded: {}", generatedUrl);
      return ResponseEntity.ok().body(new FilesResponse(true, generatedUrl, filename));
    } catch (Exception e) {
      String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      log.error(message);
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FilesResponse(false, message, null));
    }
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = this.fileStorageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = FileHelper.generateUrl(filename);
      return new FileInfo(filename, url);
    }).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = this.fileStorageService.load(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/{filename:.+}")
  public ResponseEntity<FilesResponse> deleteFile(@PathVariable String filename) {
    boolean result = this.fileStorageService.delete(filename);
    return ResponseEntity.ok().body(new FilesResponse(result, null, filename));
  }
}
