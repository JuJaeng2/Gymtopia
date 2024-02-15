package com.project.gymtopia.trainer.service.impl;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.trainer.data.entity.SpecialProgram;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.SpecialProgramDto;
import com.project.gymtopia.trainer.data.model.SpecialProgramForm;
import com.project.gymtopia.trainer.data.model.SpecialProgramList;
import com.project.gymtopia.trainer.data.model.SpecialProgramState;
import com.project.gymtopia.trainer.repository.SpecialProgramRepository;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.SpecialProgramService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecialProgramServiceImpl implements SpecialProgramService {

  private final SpecialProgramRepository specialProgramRepository;
  private final TrainerRepository trainerRepository;
  private final MemberRepository memberRepository;

  @Override
  public void registerProgram(String email, SpecialProgramForm specialProgramForm) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    specialProgramRepository.save(SpecialProgram.builder()
        .programName(specialProgramForm.getProgramName())
        .state(SpecialProgramState.RECRUITING)
        .recruitCount(specialProgramForm.getRecruitCount())
        .trainer(trainer)
        .deadlineDate(specialProgramForm.getDeadlineDate())
        .build());

  }

  @Override
  public void closeProgram(String email, long programId) {
    trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    SpecialProgram specialProgram = specialProgramRepository.findById(programId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAM_NOT_FOUND));

    specialProgram.setState(SpecialProgramState.CLOSED);
    specialProgramRepository.save(specialProgram);
  }

  @Override
  public SpecialProgramList findAllSpecialProgram(String email) {

    memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    //TODO: 페이징 처리 필요
    List<SpecialProgram> specialProgramList = specialProgramRepository.findAll();

    List<SpecialProgramDto> specialProgramDtoList =
        specialProgramList.stream()
            .map(specialProgram -> SpecialProgramDto.from(specialProgram))
            .toList();

    return SpecialProgramList.builder()
        .specialProgramList(specialProgramDtoList)
        .build();
  }

  @Override
  @Transactional
  public void applyProgram(String email, long programId) {

    log.info("==== Transaction 시작!!! ====");
    SpecialProgram specialProgram = specialProgramRepository.findByIdWithPessimisticLock(programId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAM_NOT_FOUND));

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (specialProgram.getMemberList().contains(member.getId())){
      throw new CustomException(ErrorCode.ALREADY_APPLY_PROGRAM);
    }

    if (specialProgram.getRecruitCount() == 0 || specialProgram.getState() == SpecialProgramState.CLOSED) {
      throw new CustomException(ErrorCode.CLOSED_PROGRAM);
    }

    int newRecruitCount = specialProgram.getRecruitCount() - 1;
    if (newRecruitCount == 0){
      specialProgram.setState(SpecialProgramState.CLOSED);
    }

    specialProgram.setRecruitCount(newRecruitCount);
    specialProgram.getMemberList().add(member.getId());
    specialProgramRepository.save(specialProgram);
    log.info("==== Transaction 종료!!! State : {}, Count : {}====", specialProgram.getState(), specialProgram.getRecruitCount());

  }
}
