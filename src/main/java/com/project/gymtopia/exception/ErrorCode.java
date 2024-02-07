package com.project.gymtopia.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  //Member
  WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
  REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),

  //Trainer
  TRAINER_NOT_FOUND(HttpStatus.BAD_REQUEST, "트레이너 정보를 찾을 수 업습니다..!"),

  //AWS S3 ImageException
  IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패했습니다."),
  IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "이미지 삭제에 실패했습니다."),

  //Journal
  JOURNAL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 일지입니다."),
  NOT_SAME_MEMBER_AND_WRITER(HttpStatus.BAD_REQUEST, "일지 작성자와 일지를 조회하려는 회원이 동일하지 않습니다."),
  NOT_WRITER_OF_JOURNAL(HttpStatus.BAD_REQUEST, "회원의 일지가 아닙니다."),

  //Feedback
  FEEDBACK_NOT_FOUND(HttpStatus.BAD_REQUEST, "피드백이 존재하지 않습니다."),
  NOT_WRITER_OF_FEEDBACK(HttpStatus.BAD_REQUEST, "피드백의 작성자가 아닙니다."),

  //Management
  NO_MEMBER_MANAGEMENT(HttpStatus.BAD_REQUEST, "관리중인 회원이 없습니다."),

  //Mission
  MISSION_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 수행중인 미션이 존재합니다."),
  MISSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "받은 미션이 없거나, 수행중인 미션이 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;
}
