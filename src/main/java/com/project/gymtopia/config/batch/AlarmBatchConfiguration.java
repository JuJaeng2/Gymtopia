package com.project.gymtopia.config.batch;

import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.service.AlarmService;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Trainer;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Slf4j
@RequiredArgsConstructor
public class AlarmBatchConfiguration {

  private final EntityManagerFactory entityManagerFactory;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final AlarmService alarmService;
  private final int CHUNK_SIZE = 5;

  @Bean
  public Job alarmJob() {
    log.info("==== 마감기한 알림 문자 Job 실행 ====");
    return new JobBuilder("sendAlarmJob", jobRepository)
        .start(alarmStep())
        .build();
  }

  @Bean
  public Step alarmStep() {
    return new StepBuilder("sendAlarmStep", jobRepository)
        .<Mission, Mission>chunk(CHUNK_SIZE, transactionManager)
        .reader(missionItemReader())
        .processor(missionItemProcessor())
        .writer(missionItemWriter())
        .build();
  }
  @Bean
  public JpaCursorItemReader<Mission> missionItemReader() {

    return new JpaCursorItemReaderBuilder<Mission>()
        .name("missionPagingReader")
        .entityManagerFactory(entityManagerFactory)
        .queryString("select m from Mission m where m.state = PROGRESSING and m.expirationDate = :aDayAgo")
        .parameterValues(Collections.singletonMap("aDayAgo", LocalDate.now().minusDays(1)))
        .build();
  }

  @Bean
  public ItemProcessor<Mission, Mission> missionItemProcessor() {
    return item -> {

      log.info("=== Processor ===");
      log.info("MissionId : {}, MemberId : {}, TrainerId : {} ", item.getId(),
          item.getMember().getId(), item.getTrainer().getId());

      Member member = item.getMember();
      Trainer trainer = item.getTrainer();
      String message = "미션 인증 마감 하루 전 입니다.";
      alarmService.sendMissionAlarm(member, trainer, message);

      return item;
    };
  }

  @Bean
  public JpaItemWriter<Mission> missionItemWriter() {
    return new JpaItemWriterBuilder<Mission>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }

}
