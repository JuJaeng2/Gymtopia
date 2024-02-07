package com.project.gymtopia.common.service;

import com.project.gymtopia.common.data.model.TrainerSearchResponse;
import com.project.gymtopia.trainer.data.model.TrainerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchTrainerService {

  TrainerProfile getTrainerInfo( long trainerId);

  Page<TrainerSearchResponse> searchTrainer(Pageable pageable);
}
