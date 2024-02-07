package com.project.gymtopia.trainer.service;

import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.data.model.TrainerUpdate;

public interface TrainerInfoService {
  TrainerResponse getTrainerInformation(String email);

  TrainerResponse updateInfo(TrainerUpdate trainerUpdate, String email);
}
