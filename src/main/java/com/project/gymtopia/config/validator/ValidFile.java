package com.project.gymtopia.config.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileValidator.class)
public @interface ValidFile {
  String message() default "파일의 개수가 맞지 않습니다.";
  long value();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
