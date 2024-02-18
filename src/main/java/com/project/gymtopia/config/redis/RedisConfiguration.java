package com.project.gymtopia.config.redis;

import com.project.gymtopia.common.service.impl.RedisAlarmSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;
  private final RedisAlarmSubscriber redisAlarmSubscriber;

  @Bean
  public RedisConnectionFactory redisConnectionFactory(){
    RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(host, port);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public ChannelTopic topic() {
    return new ChannelTopic("Alarm");
  }

  @Bean
  public RedisMessageListenerContainer redisContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory());
    container.addMessageListener(messageListenerAdapter(), topic());
    return container;
  }

  @Bean
  public MessageListenerAdapter messageListenerAdapter(){
    return new MessageListenerAdapter(redisAlarmSubscriber, "onMessage");
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(){
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MessageDto.class));
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
    return redisTemplate;
  }

}
