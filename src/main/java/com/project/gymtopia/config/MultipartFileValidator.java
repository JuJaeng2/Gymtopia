package com.project.gymtopia.config;


import com.project.gymtopia.config.validator.ValidFile;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class MultipartFileValidator implements ConstraintValidator<ValidFile, List<MultipartFile>> {

  private long maxSize;
  @Override
  public void initialize(ValidFile constraintAnnotation) {
    maxSize = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(List<MultipartFile> multipartFileList, ConstraintValidatorContext context) {
    log.info("<<<< Custom Validator MaxSize : {}>>>>", maxSize);
    log.info("현재 사이즈 : {}", multipartFileList.size());
    if (multipartFileList.size() > 2){
      throw new CustomException(ErrorCode.EXCEED_LIMIT);
    }
    return multipartFileList.size() <= maxSize;
  }
}
