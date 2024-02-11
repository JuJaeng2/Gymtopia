package com.project.gymtopia.member.service.impl;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.entity.Register;
import com.project.gymtopia.member.data.model.RegisterForm;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.repository.RegisterRepository;
import com.project.gymtopia.member.service.MemberRegisterService;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberRegisterServiceImpl implements MemberRegisterService {

  private final RegisterRepository registerRepository;
  private final MemberRepository memberRepository;
  private final TrainerRepository trainerRepository;

  @Override
  public void register(String email, long trainerId, RegisterForm registerForm) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    Trainer trainer = trainerRepository.findById(trainerId)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Register newRegister = Register.builder()
        .member(member)
        .trainer(trainer)
        .acceptYn(false)
        .startDate(registerForm.getStartDate())
        .endDate(registerForm.getEndDate())
        .registerDate(LocalDate.now())
        .build();

    registerRepository.save(newRegister);

    //TODO: 등록 요청이 왔다는 알림을 해당 트레이너에게 전송
  }

  @Override
  public void deleteAllRegisterInBatch(LocalDate oneWeekAgo){

    registerRepository.deleteAllByAcceptYnIsFalseAndRegisterDateAfter(oneWeekAgo);
    log.info("1주일이 지난 운동관리 신청 데이터를 모두 삭제하였습니다.");
  }

}
