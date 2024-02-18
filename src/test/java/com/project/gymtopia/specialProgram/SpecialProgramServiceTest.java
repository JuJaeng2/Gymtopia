//package com.project.gymtopia.specialProgram;
//
//import com.project.gymtopia.member.repository.MemberRepository;
//import com.project.gymtopia.trainer.data.entity.SpecialProgram;
//import com.project.gymtopia.trainer.repository.SpecialProgramRepository;
//import com.project.gymtopia.trainer.repository.TrainerRepository;
//import com.project.gymtopia.trainer.service.SpecialProgramService;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class SpecialProgramServiceTest {
//
//  @Autowired
//  private SpecialProgramRepository specialProgramRepository;
//
//  @Autowired
//  private TrainerRepository trainerRepository;
//
//  @Autowired
//  private MemberRepository memberRepository;
//
//  @Autowired
//  private SpecialProgramService specialProgramService;
//
//  @Test
//  @DisplayName("동시에 20개의 요청으로 선착순 모집 특별프로그램 신청")
//  void apply() throws InterruptedException {
//    //given
//    final int threadCount = 20;
//    final ExecutorService executorService = Executors.newFixedThreadPool(10);
//    final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
//
//    //when
//    for (int i = 1; i <= threadCount; i++) {
//      String email = "test" + i + "@naver.com";
//      executorService.submit(() -> {
//        try{
//          specialProgramService.applyProgram(email, 1);
//        }finally {
//          countDownLatch.countDown();
//        }
//      });
//    }
//    countDownLatch.await();
//    SpecialProgram specialProgram = specialProgramRepository.findById(1L)
//        .orElseThrow();
//
//    //then
//    Assertions.assertEquals(0, specialProgram.getRecruitCount());
//  }
//
//}
