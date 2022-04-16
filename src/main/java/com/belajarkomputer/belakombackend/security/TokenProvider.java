package com.belajarkomputer.belakombackend.security;

import com.belajarkomputer.belakombackend.config.AppProperties;
import com.belajarkomputer.belakombackend.model.entity.JwtToken;
import com.belajarkomputer.belakombackend.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class TokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

  private final AppProperties appProperties;

  private final TokenRepository tokenRepository;

  private final Key key;

  @Autowired
  public TokenProvider(AppProperties appProperties,
      TokenRepository tokenRepository) {
    this.appProperties = appProperties;
    this.tokenRepository = tokenRepository;
    this.key = new SecretKeySpec(this.appProperties.getAuth().getTokenSecret().getBytes(
        StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
  }

  public String createAccessToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    LocalDateTime expiryDate =
        LocalDateTime.now().plus(appProperties.getAuth().getAccessTokenExpirationSec(),
            ChronoUnit.SECONDS);

    return this.generateToken(userPrincipal.getId(),
        Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()));
  }

  public String createAccessTokenForGoogle(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    LocalDateTime expiryDate =
        LocalDateTime.now().plus(appProperties.getAuth().getGoogleAccessTokenExpirationSec(),
            ChronoUnit.SECONDS);

    return this.generateToken(userPrincipal.getId(),
        Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()));
  }

  public String createRefreshToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    LocalDateTime expiryDate =
        LocalDateTime.now().plus(appProperties.getAuth().getRefreshTokenExpirationSec(),
            ChronoUnit.SECONDS);

    return this.generateToken(userPrincipal.getId(),
        Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()));
  }

  public String generateToken(String id, Date expirationDate) {
    return Jwts.builder()
        .setSubject(id)
        .setExpiration(expirationDate)
        .signWith(key)
        .compact();
  }

  public String getUserIdFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(authToken);

      return !this.tokenRepository.existsById(authToken);
    } catch (SignatureException ex) {
      logger.error("Invalid Signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return false;
  }

  public String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public void invalidateToken(String token) {
    Date expirationDate = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
    long ttl = expirationDate.getTime() - new Date().getTime();
    JwtToken jwtToken = new JwtToken(token, TimeUnit.MILLISECONDS.toSeconds(ttl));
    logger.info("saving token to redis {}", jwtToken);
    this.tokenRepository.save(jwtToken);
  }
}
