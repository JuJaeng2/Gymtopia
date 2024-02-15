package com.project.gymtopia.util;

import java.util.UUID;

public class MultipartUtil {
  public static String createFileId(String originalName) {
    String random = UUID.randomUUID().toString();
    return random + originalName;
  }
}
