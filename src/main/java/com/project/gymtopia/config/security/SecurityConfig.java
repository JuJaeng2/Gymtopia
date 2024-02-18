package com.project.gymtopia.config.security;

import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequest -> {
          authorizeRequest.requestMatchers(
              "/signUp/**",
              "/signIn/**",
              "/search/**",
              "sse/**",
              "/docs/**",
              "/v3/api-docs/**",
              "/swagger-ui/**",
              "/swagger-ui"
          ).permitAll();
          authorizeRequest.requestMatchers("/trainer/**", "/withdraw/member").hasAuthority(String.valueOf(Roles.TRAINER));
          authorizeRequest.requestMatchers("/member/**", "/withdraw/trainer").hasAuthority(String.valueOf(Roles.MEMBER));
        })
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
