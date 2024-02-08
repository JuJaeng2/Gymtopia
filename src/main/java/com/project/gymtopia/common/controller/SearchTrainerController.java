package com.project.gymtopia.common.controller;

import com.project.gymtopia.common.data.model.TrainerSearchResponse;
import com.project.gymtopia.common.service.SearchTrainerService;
import com.project.gymtopia.trainer.data.model.TrainerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchTrainerController {

  private final SearchTrainerService searchTrainerService;

  /**
   * 트레이너 목록 반환하는 API
   */
  @GetMapping("/trainer")
  public ResponseEntity<?> searchTrainer(Pageable pageable){
    Page<TrainerSearchResponse> responsePage = searchTrainerService.searchTrainer(pageable);
    return ResponseEntity.ok(responsePage);
  }

  /**
   * 특정 트레이너의 정보 반환하는 API
   */
  @GetMapping("/trainer/{trainerId}")
  public ResponseEntity<?> getTrainerInfo(
      @PathVariable(value = "trainerId") long trainerId
  ){
    TrainerProfile trainerProfile = searchTrainerService.getTrainerInfo( trainerId);
    return ResponseEntity.ok(trainerProfile);
  }


}
