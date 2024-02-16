package com.project.gymtopia.config.jwt;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserDto;
import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.common.service.impl.MemberDetailsServiceImpl;
import com.project.gymtopia.common.service.impl.TrainerDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtToken {

  private Map<Roles, UserDetailsService> authMap;
  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  @PostConstruct
  void init(){
    authMap = Map.of(Roles.MEMBER, memberDetailsService, Roles.TRAINER, trainerDetailsService);
  }

  @Value("${jwt.secret}")
  private String secretKey;

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 600;
  private final TrainerDetailsServiceImpl trainerDetailsService;
  private final MemberDetailsServiceImpl memberDetailsService;

  public TokenResponse createToken(UserDto userDto, Roles role){
    Claims claims = Jwts.claims().setSubject(userDto.getName());
    claims.put("role", role);
    claims.put("email", userDto.getEmail());
    claims.put("id", userDto.getId());

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    String token = Jwts.builder().setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();

    return TokenResponse.builder().token(token).build();
  }

  public Authentication getAuthentication(String token, Roles role) {

    UserDetails userDetails =
        authMap.get(role).loadUserByUsername(getUserEmail(token));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  private String getUserEmail(String token){
    log.info("Parsing Token Email >>> " + parseClaims(token).get("email", String.class));
    return parseClaims(token).get("email", String.class);
  }

  private Claims parseClaims(String token){
    try{
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }catch (ExpiredJwtException e){
      return e.getClaims();
    }
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) return false;

    Claims claims = parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }

  public String getTokenFromRequest(HttpServletRequest request){
    String token = request.getHeader(TOKEN_HEADER);

    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
      return token.substring(TOKEN_PREFIX.length());
    }

    return null;
  }

  public  Roles getRole(String token){
    Claims claims = parseClaims(token);
    return Roles.valueOf(claims.get("role", String.class));
  }

  public long getId(String token) {
    Claims claims = parseClaims(token);
    return claims.get("id", Long.class);
  }
}
