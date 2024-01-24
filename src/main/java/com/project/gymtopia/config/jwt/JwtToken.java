package com.project.gymtopia.config.jwt;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtToken {

  @Value("${jwt.secret}")
  private String secretKey;

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
  private UserDetailsService userDetailsService;

  public TokenResponse createToken(UserDto userDto, String role){
    Claims claims = Jwts.claims().setSubject(userDto.getName());
    claims.put("role", role);

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    String token = Jwts.builder().setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();

    return TokenResponse.builder().token(token).build();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUserName(token));
    return null;
  }

  private String getUserName(String token){
    return parseClaims(token).get("role", String.class);
  }

  private Claims parseClaims(String token){
    try{
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }catch (ExpiredJwtException e){
      return e.getClaims();
    }
  }

  public boolean validateToken(String token) {
    return false;
  }
}
