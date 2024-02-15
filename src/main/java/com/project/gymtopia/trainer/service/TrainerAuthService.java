package com.project.gymtopia.trainer.service;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.member.data.model.WithdrawForm;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import com.project.gymtopia.trainer.data.model.TrainerResponse;

public interface TrainerAuthService {

  TrainerDto authenticate(String email, String password);

  TokenResponse createToken(TrainerDto trainerDto);

  TrainerResponse signUp(UserSignUpForm userSignUpForm);

  void withdraw(String email, WithdrawForm withdrawForm);
}
