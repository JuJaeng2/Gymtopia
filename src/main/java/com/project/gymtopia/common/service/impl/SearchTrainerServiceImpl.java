package com.project.gymtopia.common.service.impl;

import com.project.gymtopia.common.data.model.TrainerSearchResponse;
import com.project.gymtopia.common.service.SearchTrainerService;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerProfile;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchTrainerServiceImpl implements SearchTrainerService {

  private final TrainerRepository trainerRepository;

  @Override
  public Page<TrainerSearchResponse> searchTrainer(Pageable pageable) {
    //TODO: 최적화 필요
    Page<Trainer> trainerPage = trainerRepository.findAll(pageable);
    return trainerPage.map(trainer -> TrainerSearchResponse.from(trainer));
  }

  @Override
  public TrainerProfile getTrainerInfo(long trainerId) {

    Trainer trainer = trainerRepository.findById(trainerId)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    return TrainerProfile.builder()
        .trainerName(trainer.getName())
        .number(trainer.getNumber())
        .email(trainer.getEmail())
        .gymName(trainer.getGymName())
        .introduction(trainer.getIntroduction())
        .career(trainer.getCareer())
        .build();
  }


}
