package com.project.gymtopia.exception;

import java.io.IOException;
import lombok.Getter;

@Getter
public class ImageUploadException extends IOException {

  private final ErrorCode errorCode;

  public ImageUploadException(ErrorCode errorCode){
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
