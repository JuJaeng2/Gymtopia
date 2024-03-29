package com.project.gymtopia.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  //Member
  WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
  REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
  REMOVED_MEMBER_ACCOUNT(HttpStatus.NOT_FOUND, "탈퇴한 회원 계정입니다."),

  //Trainer
  TRAINER_NOT_FOUND(HttpStatus.NOT_FOUND, "트레이너 정보를 찾을 수 업습니다..!"),
  REMOVED_TRAINER_ACCOUNT(HttpStatus.NOT_FOUND, "탈퇴한 트레이너 계정입니다."),

  // Withdraw
  NOT_SAME_MEMBER(HttpStatus.CONFLICT, "이메일이 일치하지 않습니다."),


  //AWS S3 ImageException
  IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패했습니다."),
  IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "이미지 삭제에 실패했습니다."),

  //Journal
  JOURNAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일지입니다."),
  NOT_SAME_MEMBER_AND_WRITER(HttpStatus.BAD_REQUEST, "일지 작성자와 일지를 조회하려는 회원이 동일하지 않습니다."),
  NOT_WRITER_OF_JOURNAL(HttpStatus.BAD_REQUEST, "회원의 일지가 아닙니다."),
  WRONG_JOURNAL_TYPE(HttpStatus.BAD_REQUEST, "회원의 미션 일지만 확인할 수 있습니다."),
  EXCEED_LIMIT(HttpStatus.BAD_REQUEST, "이미지 파일은 최대 2개, 영상 파일은 최대 1개까지만 업로드 가능합니다."),

  //Feedback
  FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND, "피드백이 존재하지 않습니다."),
  NOT_WRITER_OF_FEEDBACK(HttpStatus.BAD_REQUEST, "피드백의 작성자가 아닙니다."),

  //Management
  NO_MEMBER_MANAGEMENT(HttpStatus.NOT_FOUND, "관리중인 회원이 없습니다."),
  NOT_MEMBER_OF_TRAINER(HttpStatus.CONFLICT, "트레이너의 회원이 아닙니다."),

  //Mission
  MISSION_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 수행중인 미션이 존재합니다."),
  MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "받은 미션이 없거나, 수행중인 미션이 없습니다."),
  MISSION_JOURNAL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 해당 미션에 대한 일지가 존재합니다."),

  //Review
  REVIEW_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 작성한 리뷰가 있습니다."),
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다."),
  NOT_WRITER_OF_REVIEW(HttpStatus.BAD_REQUEST, "리뷰 작성자와 리뷰를 조회하는 회원이 동일하지 않습니다."),
  NOT_SAME_TRAINER(HttpStatus.BAD_REQUEST, "해당 트레이너에대한 리뷰가 아닙니다."),
  INVALID_REVIEW(HttpStatus.BAD_REQUEST, "트레이너 정보가 다릅니다. 잘못된 리뷰 정보 입니다."),

  //Register
  REGISTER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 운동 관리 신청 취소된 요청이거나 존재하지 않는 요청입니다."),
  INVALID_REGISTER(HttpStatus.BAD_REQUEST, "해당 요청은 잘못된 요청입니다."),
  PROCESSED_REQUEST(HttpStatus.BAD_REQUEST, "이미 처리된 운동 관리 신청입니다."),

  //SSE
  SSE_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SSE 전송에 문제가 발생했습니다."),
  NOT_LOGIN(HttpStatus.NOT_FOUND, "수신자가 연결되어 있지 않습니다."),

  // SpecialProgram
  PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "특별프로그램이 존재하지 않습니다."),
  CLOSED_PROGRAM(HttpStatus.BAD_REQUEST, "특별프로그램 모집이 마감되었습니다."),
  ALREADY_APPLY_PROGRAM(HttpStatus.CONFLICT, "이미 신청한 특별프로그램입니다.");


  private final HttpStatus httpStatus;
  private final String message;
}
