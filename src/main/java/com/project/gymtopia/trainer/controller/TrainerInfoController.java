package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.data.model.TrainerUpdate;
import com.project.gymtopia.trainer.service.TrainerInfoService;
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
public class TrainerInfoController {

  private final TrainerInfoService trainerInfoService;

  @GetMapping("/info")
  public ResponseEntity<TrainerResponse> memberInformation(Authentication authentication
  ) {

    String email = getEmail(authentication);
    TrainerResponse trainerResponse = trainerInfoService.getTrainerInformation(email);
    return ResponseEntity.ok(trainerResponse);
  }

  @PutMapping("/info")
  public ResponseEntity<TrainerResponse> updateMemberInformation(
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
