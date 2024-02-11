package com.project.gymtopia.config.batch;

import com.project.gymtopia.member.data.entity.Register;
import com.project.gymtopia.member.repository.RegisterRepository;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {

  private final EntityManagerFactory entityManagerFactory;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final RegisterRepository registerRepository;
  private final int CHUNK_SIZE = 5;

  @Bean
  public Job registerJob(){
    log.info("==== Register Job 실행 ====");

    return new JobBuilder("deleteRegister", jobRepository)
        .start(registerStep())
        .build();
  }

  @Bean
  public Step registerStep(){
    return new StepBuilder("deleteRegisterStep",jobRepository)
        .<Register, Register>chunk(CHUNK_SIZE, transactionManager)
        .reader(registerItemReader())
        .writer(registerItemWriter())
        .build();
  }

  @Bean
  public JpaPagingItemReader<Register> registerItemReader(){
    JpaPagingItemReader<Register> reader = new JpaPagingItemReader<>(){
      @Override
      public int getPage(){
        return 0;
      }
    };

    reader.setQueryString("select r from Register r where r.acceptYn = false AND r.registerDate < :oneWeekAgo");
    reader.setParameterValues(Collections.singletonMap("oneWeekAgo", LocalDate.now()));
    reader.setPageSize(CHUNK_SIZE);
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setName("registerPagingReader");

    return reader;
  }

  @Bean
  public ItemWriter<Register> registerItemWriter(){
    return registers -> {
      registers.getItems()
          .forEach(register -> log.info("삭제된 Register Id : {}", register.getId()));

      List<Long> registerList = registers.getItems().stream()
          .map(register -> register.getId())
          .toList();

      registerRepository.deleteAllInQuery(registerList);
    };
  }

}
