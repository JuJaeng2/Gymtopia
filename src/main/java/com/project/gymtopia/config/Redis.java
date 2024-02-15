package com.project.gymtopia.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class Redis {

  private final RedisConnectionFactory redisConnectionFactory;
//  @Bean
//  public RedisConnectionFactory redisMessageListenerContainer(){
//
//    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//    redisStandaloneConfiguration.setHostName(hot);
//
//    return container;
//  }

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer(){

  }

}
