package com.project.gymtopia.trainer.service.impl;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserDto;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.config.jwt.JwtToken;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.TrainerAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerAuthServiceImpl implements TrainerAuthService {

  private final TrainerRepository trainerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtToken jwtToken;

  @Override
  public TrainerDto authenticate(String email, String password) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(password, trainer.getPassword())) {
      throw new CustomException(ErrorCode.WRONG_PASSWORD);
    }

    return TrainerDto.builder()
        .id(trainer.getId())
        .name(trainer.getName())
        .email(trainer.getEmail())
        .role(trainer.getRole())
        .build();
  }

  @Override
  public TokenResponse createToken(TrainerDto trainerDto) {

    return jwtToken.createToken(
        UserDto.builder()
            .id(trainerDto.getId())
            .name(trainerDto.getName())
            .email(trainerDto.getEmail())
            .build(),
        trainerDto.getRole());
  }

  @Override
  public TrainerResponse signUp(UserSignUpForm userSignUpForm) {

    if (isEmailExist(userSignUpForm.getEmail())){
      throw new CustomException(ErrorCode.REGISTERED_EMAIL);
    }

    String encodingPassword = passwordEncoder.encode(userSignUpForm.getPassword());

    Trainer newTrainer= Trainer.builder()
        .name(userSignUpForm.getName())
        .email(userSignUpForm.getEmail())
        .password(encodingPassword)
        .role(Roles.TRAINER)
        .birth(userSignUpForm.getBirth())
        .number(userSignUpForm.getNumber())
        .build();

    trainerRepository.save(newTrainer);

    return TrainerResponse.builder()
        .name(newTrainer.getName())
        .email(newTrainer.getEmail())
        .number(newTrainer.getNumber())
        .build();
  }

  private boolean isEmailExist(String email){
    return trainerRepository.existsByEmail(email);
  }

}
