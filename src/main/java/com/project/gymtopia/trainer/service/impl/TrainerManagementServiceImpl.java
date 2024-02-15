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
import com.project.gymtopia.member.data.entity.Register;
import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.member.data.model.MediaResponse;
import com.project.gymtopia.member.repository.JournalRepository;
import com.project.gymtopia.member.repository.MediaRepository;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.repository.RegisterRepository;
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
import com.project.gymtopia.trainer.data.model.RegisterManagement;
import com.project.gymtopia.trainer.repository.FeedBackRepository;
import com.project.gymtopia.trainer.repository.ManagementRepository;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.TrainerManagementService;
import com.project.gymtopia.util.MediaUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private final RegisterRepository registerRepository;

  @Override
  public MemberListResponse getMemberInfo(String email) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    List<Management> managementList = managementRepository.findAllByTrainer(trainer)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_MANAGEMENT));
    if (managementList.isEmpty()){
      throw new CustomException(ErrorCode.NO_MEMBER_MANAGEMENT);
    }

    List<ManagementDto> managementDtoList = managementList.stream()
        .map(management -> ManagementDto.from(management))
        .toList();

    return MemberListResponse.builder()
        .trainerName(trainer.getName())
        .managementList(managementDtoList)
        .build();
  }

  @Override
  public JournalList getMissionJournal(String email, long memberId) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    managementRepository.findByTrainerAndMember(trainer, member)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<Journal> journalList = journalRepository.findAllByMemberAndType(member,
        JournalType.MISSION_JOURNAL);

    if (journalList.isEmpty()){
      throw new CustomException(ErrorCode.JOURNAL_NOT_FOUND);
    }

    List<MemberJournalInfo> memberJournalInfoList = journalList.stream()
        .map(journal -> MemberJournalInfo.builder()
            .journalId(journal.getId())
            .journalTitle(journal.getTitle())
            .build())
        .collect(Collectors.toList());

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
    if (!journal.getType().equals(JournalType.MISSION_JOURNAL)){
      throw new CustomException(ErrorCode.WRONG_JOURNAL_TYPE);
    }

    Optional<FeedBack> optionalFeedBack = feedBackRepository.findByJournal(journal);
    FeedBackDto feedBackDto =
        optionalFeedBack.isPresent() ? FeedBackDto.from(optionalFeedBack.get()) : null;

//    Optional<List<Media>> optionalMediaList = mediaRepository.findAllByJournal(journal);
    List<Media> mediaList = mediaRepository.findAllByJournal(journal);
    MediaResponse mediaResponse =
        mediaList.isEmpty() ? null : MediaUtil.classifyMedia(mediaList);

    return JournalResponse.from(journal, feedBackDto, mediaResponse);

  }

  @Override
  public void writeFeedback(FeedbackForm feedBackForm, String email, long journalId) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));

    if (!journal.getType().equals(JournalType.MISSION_JOURNAL)){
      throw new CustomException(ErrorCode.WRONG_JOURNAL_TYPE);
    }

    FeedBack feedBack = FeedBack.builder()
        .contents(feedBackForm.getContents())
        .trainer(trainer)
        .journal(journal)
        .build();

    feedBackRepository.save(feedBack);

    journal.getMission().setState(feedBackForm.getState());
    journalRepository.save(journal);
  }

  @Override
  public void updateFeedback(FeedbackForm feedbackForm, String email, long journalId) {

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(ErrorCode.JOURNAL_NOT_FOUND));

    FeedBack feedBack = feedBackRepository.findByJournal(journal)
        .orElseThrow(() -> new CustomException(ErrorCode.FEEDBACK_NOT_FOUND));
    if (!feedBack.getTrainer().equals(trainer)) {
      throw new CustomException(ErrorCode.NOT_WRITER_OF_FEEDBACK);
    }


    feedBack.setContents(feedbackForm.getContents());
    feedBackRepository.save(feedBack);

    journal.getMission().setState(feedbackForm.getState());
    journalRepository.save(journal);
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

    LocalDate now = LocalDate.now();

    Mission newMission = Mission.builder()
        .member(member)
        .trainer(trainer)
        .title(missionForm.getTitle())
        .contents(missionForm.getContents())
        .state(MissionState.PROGRESSING)
        .createDate(now)
        .expirationDate(now.plusDays(missionForm.getPeriod()))
        .build();

    missionRepository.save(newMission);
  }

  @Override
  public boolean manageRegister(String email, long registerId, RegisterManagement registerManagement) {
    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Register register = registerRepository.findById(registerId)
        .orElseThrow(() -> new CustomException(ErrorCode.REGISTER_NOT_FOUND));

    if (register.isAcceptYn() == registerManagement.isAccepted()){
      throw new CustomException(ErrorCode.PROCESSED_REQUEST);
    }

    if (!register.getTrainer().equals(trainer)){
      throw new CustomException(ErrorCode.INVALID_REGISTER);
    }

    Management newManagement = Management.builder()
        .member(register.getMember())
        .trainer(trainer)
        .registerDate(register.getRegisterDate())
        .startDate(register.getStartDate())
        .endDate(register.getEndDate())
            .build();
    managementRepository.save(newManagement);


    register.setAcceptYn(registerManagement.isAccepted());
    register.setAcceptedDate(LocalDate.now());

    registerRepository.save(register);

    return registerManagement.isAccepted();
  }
}
