package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.data.model.TrainerUpdate;
import com.project.gymtopia.trainer.service.TrainerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer")
@Tag(name = "Trainer Information Controller", description = "트레이너 자신의 정보 관련 컨트롤러")
public class TrainerInfoController {

  private final TrainerInfoService trainerInfoService;

  @GetMapping("/info")
  @Operation(summary = "트레이너 자신의 정보 가져오기")
  public ResponseEntity<TrainerResponse> information(Authentication authentication
  ) {

    String email = getEmail(authentication);
    TrainerResponse trainerResponse = trainerInfoService.getTrainerInformation(email);
    return ResponseEntity.ok(trainerResponse);
  }

  @PutMapping("/info")
  @Operation(summary = "트레이너 자신의 정보 수정")
  public ResponseEntity<TrainerResponse> updateInformation(
      @RequestBody TrainerUpdate trainerUpdate, Authentication authentication
  ){
    String email = getEmail(authentication);
    TrainerResponse trainerResponse = trainerInfoService.updateInfo(trainerUpdate, email);
    return ResponseEntity.ok(trainerResponse);
  }

  private String getEmail(Authentication authentication){
    return authentication.getName();
  }

}
