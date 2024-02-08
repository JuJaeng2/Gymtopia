package com.project.gymtopia.member;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.entity.Review;
import com.project.gymtopia.member.data.model.ReviewForm;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.repository.ReviewRepository;
import com.project.gymtopia.member.service.impl.MemberReviewServiceImpl;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.repository.TrainerRepository;
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
public class MemberReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private MemberRepository memberRepository;
  @Mock
  private TrainerRepository trainerRepository;

  @InjectMocks
  private MemberReviewServiceImpl memberReviewService;

  Trainer trainer;
  Member member;
  String memberEmail;
  ReviewForm reviewForm;
  Review review;

  @BeforeEach
  void setUp(){
    memberEmail = "member@naver.com";
    trainer = Trainer.builder()
        .id(1L)
        .name("Trainer")
        .email("trainer@naver.com")
        .build();

    member = Member.builder()
        .id(1L)
        .name("Member")
        .email("member@naver.com")
        .build();

    reviewForm = ReviewForm.builder()
        .contents("Test reviewForm contents")
        .build();

    review = Review.builder()
        .id(1L)
        .trainer(trainer)
        .member(member)
        .contents("Test review contents")
        .build();
  }

  @Test
  void 리뷰_작성_성공(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findByMemberAndTrainer(
        Mockito.any(Member.class), Mockito.any(Trainer.class)))
        .thenReturn(Optional.empty());
    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
    //when
    memberReviewService.writeReview(memberEmail, reviewForm, 1L);
    //then
    Mockito.verify(reviewRepository).save(reviewCaptor.capture());
    Review savedReview = reviewCaptor.getValue();

    Assertions.assertEquals(member, savedReview.getMember());
    Assertions.assertEquals(trainer, savedReview.getTrainer());
    Assertions.assertEquals(reviewForm.getContents(), savedReview.getContents());
  }
  @Test
  void 리뷰_작성_실패1(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.writeReview(memberEmail, reviewForm, 1L));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_작성_실패2(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.writeReview(memberEmail, reviewForm, 1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_작성_실패3(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findByMemberAndTrainer(
            Mockito.any(Member.class), Mockito.any(Trainer.class)))
        .thenReturn(Optional.of(Review.builder().build()));
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.writeReview(memberEmail, reviewForm, 1L));
    //then
    Assertions.assertEquals(ErrorCode.REVIEW_ALREADY_EXIST.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_수정_성공(){
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));

    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
    //when
    memberReviewService.updateReview(memberEmail, reviewForm, 1L, 1L);
    //then
    Mockito.verify(reviewRepository).save(reviewCaptor.capture());
    Review savedReview = reviewCaptor.getValue();

    Assertions.assertEquals(member, savedReview.getMember());
    Assertions.assertEquals(trainer, savedReview.getTrainer());
    Assertions.assertEquals(reviewForm.getContents(), savedReview.getContents());
  }
  @Test
  void 리뷰_수정_실패1(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.updateReview(memberEmail, reviewForm, 1L, 1L));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_수정_실패2(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.updateReview(memberEmail, reviewForm, 1L, 1L));

    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_수정_실패3(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.updateReview(memberEmail, reviewForm, 1L, 1L));

    //then
    Assertions.assertEquals(ErrorCode.REVIEW_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_수정_실패4(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(Member.builder().id(2L).build()));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.updateReview(memberEmail, reviewForm, 1L, 1L));

    //then
    Assertions.assertEquals(ErrorCode.NOT_WRITER_OF_REVIEW.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_수정_실패5(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(trainerRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(Trainer.builder().id(2L).build()));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.updateReview(memberEmail, reviewForm, 1L, 1L));

    //then
    Assertions.assertEquals(ErrorCode.NOT_SAME_TRAINER.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_삭제_성공(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));

    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
    //when
    memberReviewService.deleteReview(memberEmail, 1L);
    //then
    Mockito.verify(reviewRepository).delete(reviewCaptor.capture());
    Review deletedReview = reviewCaptor.getValue();

    Assertions.assertEquals(member, deletedReview.getMember());
    Assertions.assertEquals(trainer, deletedReview.getTrainer());
    Assertions.assertEquals(review.getContents(), deletedReview.getContents());
  }
  @Test
  void 리뷰_삭제_실패1(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(member));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.deleteReview(memberEmail, 1L));
    //then
    Assertions.assertEquals(ErrorCode.REVIEW_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_삭제_실패2(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.deleteReview(memberEmail, 1L));
    //then
    Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  void 리뷰_삭제_실패3(){
    //given
    Mockito.when(memberRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(Member.builder().id(2L).build()));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));

    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> memberReviewService.deleteReview(memberEmail, 1L));

    //then
    Assertions.assertEquals(ErrorCode.NOT_WRITER_OF_REVIEW.getMessage(), exception.getMessage());
  }
}
