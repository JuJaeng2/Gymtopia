package com.project.gymtopia.common.service;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.TrainerSecurityDto;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("trainerDetailsService")
public class TrainerDetailsServiceImpl implements UserDetailsService {

  private final TrainerRepository trainerRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return TrainerSecurityDto.builder()
        .name(trainer.getName())
        .email(trainer.getEmail())
        .role(List.of(String.valueOf(trainer.getRole())))
        .build();
  }
}
