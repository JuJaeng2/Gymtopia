package com.project.gymtopia.trainer.service.impl;

import com.project.gymtopia.common.data.JournalType;
import com.project.gymtopia.common.data.MissionState;
import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.repository.MissionRepository;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.member.data.entity.Media;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.member.data.model.MediaResponse;
import com.project.gymtopia.member.repository.JournalRepository;
import com.project.gymtopia.member.repository.MediaRepository;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.trainer.data.entity.FeedBack;
import com.project.gymtopia.trainer.data.entity.Management;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.FeedBackDto;
import com.project.gymtopia.trainer.data.model.FeedbackForm;
import com.project.gymtopia.trainer.data.model.JournalList;
import com.project.gymtopia.trainer.data.model.ManagementDto;
import com.project.gymtopia.trainer.data.model.MemberJournalInfo;
import com.project.gymtopia.trainer.data.model.MemberListResponse;
import com.project.gymtopia.trainer.data.model.MissionForm;
import com.project.gymtopia.trainer.repository.FeedBackRepository;
import com.project.gymtopia.trainer.repository.ManagementRepository;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.TrainerManagementService;
import com.project.gymtopia.util.MediaUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerManagementServiceImpl implements TrainerManagementService {

  private final TrainerRepository trainerRepository;
  private final ManagementRepository managementRepository;
  private final MemberRepository memberRepository;
  private final JournalRepository journalRepository;
  private final MediaRepository mediaRepository;
  private final FeedBackRepository feedBackRepository;
  private final MissionRepository missionRepository;

  @Override
  public MemberListResponse getMemberInfo(String email) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    List<Management> managementList = managementRepository.findAllByTrainer(trainer)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_MANAGEMENT));

    List<ManagementDto> managementDtoList = new ArrayList<>();
    for (Management management : managementList){
      managementDtoList.add(ManagementDto.builder()
              .memberName(management.getMember().getName())
              .email(management.getMember().getEmail())
              .number(management.getMember().getNumber())
              .address(management.getMember().getAddress())
              .birth(management.getMember().getBirth())
              .registerDate(management.getRegisterDate())
              .endDate(management.getEndDate())
          .build());
    }

    return MemberListResponse.builder()
        .trainerName(trainer.getName())
        .managementList(managementDtoList)
        .build();
  }

  @Override
  public JournalList getJournal(String email, long memberId) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    managementRepository.findByTrainerAndMember(trainer, member)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<Journal> journalList = journalRepository.findAllByMember(member)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));

    List<MemberJournalInfo> memberJournalInfoList = journalList.stream()
        .filter(journal -> journal.getType().equals(JournalType.MISSION_JOURNAL))
        .map(journal -> MemberJournalInfo.builder()
            .journalId(journal.getId())
            .journalTitle(journal.getTitle())
            .build())
        .toList();

    return JournalList.builder()
        .memberName(member.getName())
        .memberJournalInfoList(memberJournalInfoList)
        .build();
  }

  @Override
  public JournalResponse getJournalInfo(String email, long memberId, long journalId) {


    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    managementRepository.findByTrainerAndMember(trainer, member)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));
    if (!journal.getMember().getEmail().equals(member.getEmail())){
      throw new CustomException(ErrorCode.NOT_WRITER_OF_JOURNAL);
    }

    Optional<FeedBack> optionalFeedBack = feedBackRepository.findByJournal(journal);
    FeedBackDto feedBackDto =
        optionalFeedBack.isPresent() ? FeedBackDto.from(optionalFeedBack.get()) : null;

    Optional<List<Media>> optionalMediaList = mediaRepository.findALlByJournal(journal);
    MediaResponse mediaResponse =
        optionalMediaList.isPresent() ? MediaUtil.classifyMedia(optionalMediaList.get()) : null;

    return JournalResponse.builder()
        .title(journal.getTitle())
        .contents(journal.getContents())
        .journalType(journal.getType())
        .mission(journal.getMission())
        .feedBackDto(feedBackDto)
        .mediaResponse(mediaResponse)
        .createDateTime(journal.getCreateDateTime())
        .build();

  }

  @Override
  public boolean writeFeedback(FeedbackForm feedBackForm, String email, long journalId) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));

    FeedBack feedBack = FeedBack.builder()
        .contents(feedBackForm.getContents())
        .trainer(trainer)
        .journal(journal)
        .build();

    feedBackRepository.save(feedBack);

    journal.getMission().setState(feedBackForm.getState());
    journalRepository.save(journal);

    return true;
  }

  @Override
  public boolean updateFeedback(FeedbackForm feedbackForm, String email, long journalId) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));

    FeedBack feedBack = feedBackRepository.findByJournal(journal)
        .orElseThrow(() -> new CustomException(ErrorCode.FEEDBACK_NOT_FOUND));
    if (!feedBack.getTrainer().equals(trainer)){
      throw new CustomException(ErrorCode.NOT_WRITER_OF_FEEDBACK);
    }

    feedBack.setContents(feedbackForm.getContents());
    feedBackRepository.save(feedBack);

    journal.getMission().setState(feedbackForm.getState());
    journalRepository.save(journal);

    return true;
  }

  @Override
  public boolean deleteFeedback(String email, long journalId) {
    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));

    FeedBack feedBack = feedBackRepository.findByJournal(journal)
        .orElseThrow(() -> new CustomException(ErrorCode.FEEDBACK_NOT_FOUND));
    if (!feedBack.getTrainer().equals(trainer)){
      throw new CustomException(ErrorCode.NOT_WRITER_OF_FEEDBACK);
    }

    feedBackRepository.delete(feedBack);

    return true;
  }

  @Override
  public void giveMission(MissionForm missionForm, String email, long memberId) {
    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Member member = memberRepository.findById(memberId)
        .orElseThrow( () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Mission newMission = Mission.builder()
        .member(member)
        .trainer(trainer)
        .title(missionForm.getTitle())
        .contents(missionForm.getContents())
        .state(MissionState.PROGRESSING)
        .expirationDateTime(getExpirationDateTime(missionForm.getMonths()))
        .build();

    missionRepository.save(newMission);
  }

  private LocalDateTime getExpirationDateTime(String months){
    int idx = months.lastIndexOf("개월");
    int period = Integer.parseInt(months.substring(0, idx));

    return LocalDateTime.now().plusMonths(period);
  }


}
