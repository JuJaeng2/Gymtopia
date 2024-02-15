package com.project.gymtopia.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException exception){
        return new ResponseEntity<>(exception.getMessage(),exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<?> imageUploadExceptionHandler(ImageUploadException exception){
        return new ResponseEntity<>(exception.getMessage(),exception.getErrorCode().getHttpStatus());
    }
}
