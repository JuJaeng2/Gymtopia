package com.project.gymtopia.config.jwt;

import com.project.gymtopia.common.roles.Roles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final JwtToken jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = getTokenFromRequest(request);

    if (token != null && jwtTokenProvider.validateToken(token)){
      Roles role = jwtTokenProvider.getRole(token);
      Authentication authentication = jwtTokenProvider.getAuthentication(token, role);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);

  }

  private String getTokenFromRequest(HttpServletRequest request){
    String token = request.getHeader(TOKEN_HEADER);

    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
      return token.substring(TOKEN_PREFIX.length());
    }

    return null;
  }
}