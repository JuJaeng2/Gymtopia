package com.project.gymtopia.trainer;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Review;
import com.project.gymtopia.member.repository.ReviewRepository;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.ReviewResponse;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.impl.TrainerReviewServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TrainerReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private TrainerRepository trainerRepository;
  @InjectMocks
  private TrainerReviewServiceImpl trainerReviewService;

  private Trainer trainer;
  private String email = "trainer@naver.com";
  private Review review;

  @BeforeEach
  void setUp() {
    trainer = Trainer.builder()
        .id(1L)
        .name("Trainer")
        .email(email)
        .build();
    review = Review.builder()
        .id(1L)
        .contents("Test Review")
        .trainer(trainer)
        .build();

  }

  @Test
  @DisplayName("모든 리뷰 가져오기 성공")
  void getAllReview() {
    //given
    List<Review> reviewList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      reviewList.add(Review.builder()
          .id(i)
          .trainer(trainer)
          .build());
    }

    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));

    Pageable pageable = PageRequest.of(0, 4);

    Page<Review> reviewPage = new PageImpl<>(List.of(), pageable, reviewList.size());

    Mockito.when(reviewRepository.findAllByTrainer(
            Mockito.any(Trainer.class),
            Mockito.any(Pageable.class)))
        .thenReturn(reviewPage);
    //when
    Page<ReviewResponse> allReview = trainerReviewService.getAllReview(email, pageable);
    //then
    Assertions.assertEquals(3, allReview.getTotalPages());
    Assertions.assertEquals(10, allReview.getTotalElements());
  }

  @Test
  @DisplayName("모든 리뷰 가져오기 실패1")
  void getAllReview_fail1() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    Pageable pageable = PageRequest.of(0, 4);
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerReviewService.getAllReview(email, pageable));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  @DisplayName("특정 리뷰 삭제 성공")
  void deleteReview_success() {
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));

    ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);

    //when
    trainerReviewService.deleteReview(email, 1L);

    //then
    Mockito.verify(reviewRepository).delete(reviewCaptor.capture());
    Review deltedReview = reviewCaptor.getValue();

    Assertions.assertEquals(review.getId(), deltedReview.getId());
    Assertions.assertEquals(review.getTrainer(), deltedReview.getTrainer());
    Assertions.assertEquals(review.getContents(), deltedReview.getContents());
  }
  @Test
  @DisplayName("특정 리뷰 삭제 실패1")
  void deleteReview_fail1(){
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerReviewService.deleteReview(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.TRAINER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  @DisplayName("특정 리뷰 삭제 실패2")
  void deleteReview_fail2(){
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(trainer));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerReviewService.deleteReview(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.REVIEW_NOT_FOUND.getMessage(), exception.getMessage());
  }
  @Test
  @DisplayName("특정 리뷰 삭제 실패3")
  void deleteReview_fail3(){
    //given
    Mockito.when(trainerRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(Trainer.builder().id(2L).build()));
    Mockito.when(reviewRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(review));
    //when
    Throwable exception = Assertions.assertThrows(CustomException.class,
        () -> trainerReviewService.deleteReview(email, 1L));
    //then
    Assertions.assertEquals(ErrorCode.INVALID_REVIEW.getMessage(), exception.getMessage());
  }


}
