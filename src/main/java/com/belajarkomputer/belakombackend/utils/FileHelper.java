package com.belajarkomputer.belakombackend.utils;

import com.belajarkomputer.belakombackend.controller.FilesController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Date;

public class FileHelper {
  public static String generateFilename(String originalFilename) {
    String extension;
    String filename;
    if (originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
      filename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
    } else {
      return originalFilename;
    }
    filename = filename.replaceAll(" ", "-");
    filename = filename.replaceAll("[^A-z0-9_-]","");
    return filename + "-" + new Date().getTime() + "." + extension;
  }

  public static String generateUrl(String filename) {
    return MvcUriComponentsBuilder
        .fromMethodName(FilesController.class, "getFile", filename)
        .build()
        .toString();
  }
}
