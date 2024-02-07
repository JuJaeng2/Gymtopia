package com.project.gymtopia.trainer.service.impl;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.data.model.TrainerUpdate;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.TrainerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerInfoServiceImpl implements TrainerInfoService {

  private final TrainerRepository trainerRepository;
  private final PasswordEncoder passwordEncoder;

  public TrainerResponse getTrainerInformation(String email){

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    return TrainerResponse.builder()
        .name(trainer.getName())
        .email(trainer.getEmail())
        .number(trainer.getNumber())
        .birth(trainer.getBirth())
        .gymName(trainer.getGymName())
        .introduction(trainer.getIntroduction())
        .career(trainer.getCareer())
        .build();

  }

  @Override
  public TrainerResponse updateInfo(TrainerUpdate trainerUpdate, String email) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    trainer.setNumber(trainerUpdate.getNumber());
    trainer.setBirth(trainerUpdate.getBirth());
    trainer.setGymName(trainerUpdate.getGymName());
    trainer.setIntroduction(trainerUpdate.getIntroduction());
    trainer.setCareer(trainerUpdate.getCareer());
    trainer.setPassword(passwordEncoder.encode(trainerUpdate.getPassword()));

    Trainer updatedTrainer = trainerRepository.save(trainer);

    return TrainerResponse.builder()
        .name(updatedTrainer.getName())
        .email(updatedTrainer.getEmail())
        .number(updatedTrainer.getNumber())
        .birth(updatedTrainer.getBirth())
        .gymName(updatedTrainer.getGymName())
        .introduction(updatedTrainer.getIntroduction())
        .career(updatedTrainer.getCareer())
        .build();
  }

}
