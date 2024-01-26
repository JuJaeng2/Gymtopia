package com.project.gymtopia.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  //Member
  WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저 정보를 찾을 수 없습니다."),
  REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
