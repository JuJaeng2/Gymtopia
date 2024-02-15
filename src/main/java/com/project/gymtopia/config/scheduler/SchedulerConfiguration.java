package com.project.gymtopia.config.scheduler;

import com.project.gymtopia.config.batch.AlarmBatchConfiguration;
import com.project.gymtopia.config.batch.BatchConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

  private final JobLauncher jobLauncher;
  private final BatchConfiguration batchConfiguration;
  private final AlarmBatchConfiguration alarmBatchConfiguration;

  @Scheduled(cron = "0 0 0 4 * *")
  public void deleteRegisterBatch()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
      JobParametersInvalidException, JobRestartException {

    jobLauncher
        .run(
            batchConfiguration.registerJob(),
            new JobParametersBuilder()
                .addString("timeStamp", String.valueOf(System.currentTimeMillis()))
                .toJobParameters()
        );
  }

  @Scheduled(cron = "0 0 1 * * *")
  public void sendAlarmBatch()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
      JobParametersInvalidException, JobRestartException {
    jobLauncher
        .run(
            alarmBatchConfiguration.alarmJob(),
            new JobParametersBuilder()
                .addString("timeStamp", String.valueOf(System.currentTimeMillis()))
                .toJobParameters()
        );
  }
}
