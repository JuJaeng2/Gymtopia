package com.project.gymtopia.trainer;

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
import com.project.gymtopia.member.repository.JournalRepository;
import com.project.gymtopia.member.repository.MediaRepository;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.trainer.data.entity.FeedBack;
import com.project.gymtopia.trainer.data.entity.Management;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.FeedbackForm;
import com.project.gymtopia.trainer.data.model.JournalList;
import com.project.gymtopia.trainer.data.model.MemberListResponse;
import com.project.gymtopia.trainer.data.model.MissionForm;
import com.project.gymtopia.trainer.repository.FeedBackRepository;
import com.project.gymtopia.trainer.repository.ManagementRepository;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.impl.TrainerManagementServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerManagementServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private ManagementRepository managementRepository;

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private JournalRepository journalRepository;
  @Mock
  private MediaRepository mediaRepository;
  @Mock
  private MissionRepository missionRepository;
  @Mock
  private FeedBackRepository feedBackRepository;
  @InjectMocks
  private TrainerManagementServiceImpl trainerManagementService;


  String email;
  Trainer trainer;
  Member member;
  Management management;
  List<Journal> journalList = new ArrayList<>();
  Mission mission;
  Journal journal;
  FeedBack feedBack;
  List<Media> mediaList = new ArrayList<>();
  FeedbackForm feedbackForm;
  MissionForm missionForm;

  @BeforeEach
  void setUp() {
    email = "test@nager.com";
    trainer = Trainer.builder()
        .id(1L)
        .name("홍길동")
        .build();
    member = Member.builder()
        .name("길동제자")
        .email(email)
        .build();
    management = Management.builder()
        .id(1L)
        .member(member)
        .trainer(trainer)
        .registerDate(LocalDate.parse("2023-10-10"))
        .endDate(LocalDate.now())
        .build();

    mission = Mission.builder()
        .id(1L)
        .member(member)
        .trainer(trainer)
        .title("Test mission")
        .contents("Test mission detail")
        .state(MissionState.PROGRESSING)
        .createDateTime(LocalDateTime.now())
        .build();

    journal = Journal.builder()
        .id(1L)
        .title("Test Journal")
        .member(member)
        .mission(mission)
        .type(JournalType.MISSION_JOURNAL)
        .contents("Test journal contents")
        .build();

    journalList.add(journal);

    feedBack = FeedBack.builder()
        .id(1L)
        .contents("Test feedBack")
        .trainer(trainer)
        .journal(journal)
        .build();

    Media media1 = Media.builder()
        .id(1L)
        .journal(journal)
        .url("http://testUrl1.com")
        .type("image/jpg")
        .build();
    Media media2 = Media.builder()
        .id(2L)
        .journal(journal)
        .url("http://testUrl2.com")
        .type("video/mp4")
        .build();
    mediaList.add(media1);
    mediaList.add(media2);

    feedbackForm = FeedbackForm.builder()
        .state(MissionState.SUCCESS)
        .contents("Test feedback contents")
        .build();

    missionForm = MissionForm.builder()
        .title("Test mission form")
        .contents("Test mission form contents")
        .months("2개월")
        .build();

  }

  @Test
  void 회원_목록_불러오기_성공() {
    //given

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(managementRepository.findAllByTrainer(Mockito.any(Trainer.class)))
        .thenReturn(Optional.of(List.of(management)));

    //when
    MemberListResponse memberListResponse = trainerManagementService.getMemberInfo(email);
    //then
    Assertions.assertEquals(trainer.getName(), memberListResponse.getTrainerName());
    Assertions.assertEquals(member.getName(),
        memberListResponse.getManagementList().get(0).getMemberName());
  }
  @Test
  void 회원목록_불러오기_실패1() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(
        CustomException.class, () -> trainerManagementService.getMemberInfo(email));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 회원목록_불러오기_실패2() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(managementRepository.findAllByTrainer(Mockito.any(Trainer.class)))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(
        CustomException.class, () -> trainerManagementService.getMemberInfo(email));
    //then
    Assertions.assertEquals(ErrorCode.NO_MEMBER_MANAGEMENT.getMessage(), exception.getMessage());
  }
  @Test
  void 특정회원의_일지리스트_가져오기_성공() {
    //when

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
    Mockito.when(managementRepository.findByTrainerAndMember(trainer, member))
        .thenReturn(Optional.of(Management.builder().build()));
    Mockito.when(journalRepository.findAllByMember(member))
        .thenReturn(Optional.of(journalList));
    //given
    JournalList journalListResult = trainerManagementService.getJournal(email, 1L);

    //then
    Assertions.assertEquals("길동제자", journalListResult.getMemberName());
    Assertions.assertEquals("Test Journal",
        journalListResult.getMemberJournalInfoList().get(0).getJournalTitle());
    Assertions.assertEquals(1L, journalListResult.getMemberJournalInfoList().get(0).getJournalId());
  }
  @Test
  void 특정회원의_일지리스트_가져오기_실패1() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournal(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 특정회원의_일지리스트_가져오기_실패2() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournal(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 특정회원의_일지리스트_가져오기_실패3() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(member));
    Mockito.when(managementRepository.findByTrainerAndMember(Mockito.any(Trainer.class),
            Mockito.any(Member.class)))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournal(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 특정회원의_일지리스트_가져오기_실패4() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(member));
    Mockito.when(managementRepository.findByTrainerAndMember(trainer, member))
        .thenReturn(Optional.of(Management.builder().build()));
    Mockito.when(journalRepository.findAllByMember(Mockito.any(Member.class)))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournal(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.JOURNAL_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 특정회원의_특정일지정보_가져오기_성공() {
    //when
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
    Mockito.when(managementRepository.findByTrainerAndMember(trainer, member))
        .thenReturn(Optional.of(Management.builder().build()));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(journal));
    Mockito.when(feedBackRepository.findByJournal(Mockito.any(Journal.class)))
        .thenReturn(Optional.of(feedBack));
    Mockito.when(mediaRepository.findALlByJournal(Mockito.any(Journal.class)))
        .thenReturn(Optional.of(mediaList));
    //given
    JournalResponse journalResponse = trainerManagementService.getJournalInfo(email, 1L, 1L);

    //then
    Assertions.assertEquals("Test Journal", journalResponse.getTitle());
    Assertions.assertEquals(JournalType.MISSION_JOURNAL, journalResponse.getJournalType());
    Assertions.assertEquals("Test journal contents", journalResponse.getContents());
  }
  @Test
  void 특정회원의_특정일지정보_실패1() {
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournalInfo(email, 1L, 1L));

    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 특정회원의_특정일지정보_실패2() {
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournalInfo(email, 1L, 1L));

    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 특정회원의_특정일지정보_실패3() {
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(member));
    Mockito.when(managementRepository.findByTrainerAndMember(Mockito.any(Trainer.class),
            Mockito.any(Member.class)))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournalInfo(email, 1L, 1L));

    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 특정회원의_특정일지정보_실패4() {
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(member));
    Mockito.when(managementRepository.findByTrainerAndMember(Mockito.any(Trainer.class),
            Mockito.any(Member.class)))
        .thenReturn(Optional.of(Management.builder().build()));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournalInfo(email, 1L, 1L));

    Assertions.assertEquals(ErrorCode.JOURNAL_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 특정회원의_특정일지정보_실패5() {
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(Member.builder()
            .email("test1@naver.com")
            .build()));
    Mockito.when(managementRepository.findByTrainerAndMember(Mockito.any(Trainer.class),
            Mockito.any(Member.class)))
        .thenReturn(Optional.of(Management.builder().build()));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(journal));

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.getJournalInfo(email, 1L, 1L));

    Assertions.assertEquals(ErrorCode.NOT_WRITER_OF_JOURNAL.getMessage(), exception.getMessage());

  }
  @Test
  void 피드백_작성_성공() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(journal));
    //when
    boolean result = trainerManagementService.writeFeedback(feedbackForm, email, 1L);

    //then
    Assertions.assertTrue(result);
  }
  @Test
  void 피드백_작성_실패1() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.writeFeedback(feedbackForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 피드백_작성_실패2() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.writeFeedback(feedbackForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.JOURNAL_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 피드백_업데이트_성공() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(1L))
        .thenReturn(Optional.of(journal));
    Mockito.when(feedBackRepository.findByJournal(Mockito.any(Journal.class)))
        .thenReturn(Optional.of(feedBack));
    //when
    boolean result = trainerManagementService.updateFeedback(feedbackForm, email, 1L);
    //then
    Assertions.assertTrue(result);
  }
  @Test
  void 피드백_업데이트_실패1() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.updateFeedback(feedbackForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 피드백_업데이트_실패2() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(1L))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.updateFeedback(feedbackForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.JOURNAL_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 피드백_업데이트_실패3() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(1L))
        .thenReturn(Optional.of(journal));
    Mockito.when(feedBackRepository.findByJournal(Mockito.any(Journal.class)))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.updateFeedback(feedbackForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.FEEDBACK_NOT_FOUND.getMessage(), exception.getMessage());

  }
  @Test
  void 피드백_삭제_성공(){
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(journal));
    Mockito.when(feedBackRepository.findByJournal(journal))
        .thenReturn(Optional.of(feedBack));

    boolean result = trainerManagementService.deleteFeedback(email, 1L);

    Assertions.assertTrue(result);
  }
  @Test
  void 피드백_삭제_실패1(){
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.deleteFeedback(email, 1L));

    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 피드백_삭제_실패2(){
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.deleteFeedback(email, 1L));

    Assertions.assertEquals(ErrorCode.JOURNAL_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 피드백_삭제_실패3(){
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(journal));
    Mockito.when(feedBackRepository.findByJournal(journal))
        .thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.deleteFeedback(email, 1L));

    Assertions.assertEquals(ErrorCode.FEEDBACK_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 피드백_삭제_실패4(){
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(journalRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(journal));
    Mockito.when(feedBackRepository.findByJournal(journal))
        .thenReturn(Optional.of(FeedBack.builder()
            .trainer(Trainer.builder().name("Trainer").build())
            .build()));

    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.deleteFeedback(email, 1L));

    Assertions.assertEquals(ErrorCode.NOT_WRITER_OF_FEEDBACK.getMessage(), exception.getMessage());
  }

  @Test
  void 미션_부여_성공(){
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(member));

    ArgumentCaptor<Mission> missionCaptor = ArgumentCaptor.forClass(Mission.class);

    //when
    trainerManagementService.giveMission(missionForm, email, 1L);

    //then
    Mockito.verify(missionRepository).save(missionCaptor.capture());
    Mission savedMission = missionCaptor.getValue();

    Assertions.assertEquals(trainer, savedMission.getTrainer());
    Assertions.assertEquals(member, savedMission.getMember());
    Assertions.assertEquals(missionForm.getTitle(), savedMission.getTitle());
    Assertions.assertEquals(missionForm.getContents(), savedMission.getContents());
    Assertions.assertEquals(MissionState.PROGRESSING, savedMission.getState());

  }

  @Test
  void 미션_부여_실패1(){
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.giveMission(missionForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void 미션_부여_실패2(){
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(memberRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerManagementService.giveMission(missionForm, email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
}