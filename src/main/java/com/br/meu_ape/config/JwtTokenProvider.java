package com.br.meu_ape.config;

import com.br.meu_ape.exception.GlobalExceptionHandler;
import com.br.meu_ape.model.Usuario;
import com.br.meu_ape.model.UsuarioRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Unwrapped;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long jwtExpirationInMs;
  private final long refreshExpirationInMs;

  public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String jwtSecret,
                          @Value("${security.jwt.token.expire-length}") long jwtExpirationInMs,
                          @Value("${security.jwt.token.expire-length}") long refreshExpirationInMs) {
    this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    this.jwtExpirationInMs = jwtExpirationInMs;
    this.refreshExpirationInMs = refreshExpirationInMs;
  }

  /**
   * Gera o Access Token principal
   */
  public String generateToken(Authentication authentication, Usuario usuario) {
    return buildToken(authentication.getName(), jwtExpirationInMs, usuario.getUsuarioRoles());
  }

  /**
   * Gera o Refresh Token (geralmente com menos claims e maior validade)
   */
  public String generateRefreshToken(Authentication authentication) {
    return buildToken(authentication.getName(), refreshExpirationInMs, List.of());
  }

  private String buildToken(String subject, long expirationTime, List<UsuarioRole> roles) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
            .subject(subject)
            .issuedAt(now)
            .claim("roles", roles)
            .expiration(expiryDate)
            .signWith(secretKey) // JJWT detecta o algoritmo automaticamente pela chave
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      String cleanedToken = cleanToken(token);
      Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(cleanedToken);
      return true;

    } catch (ExpiredJwtException e) {
      return false;

    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
      throw new GlobalExceptionHandler.CustomException("Invalid JWT token", HttpStatus.UNAUTHORIZED);

    } catch (IllegalArgumentException e) {
      throw new GlobalExceptionHandler.CustomException("JWT token is null or empty", HttpStatus.BAD_REQUEST);

    } catch (JwtException e) {
      throw new GlobalExceptionHandler.CustomException("JWT processing error", HttpStatus.UNAUTHORIZED);
    }
  }

  public String getUsernameFromToken(String token) {
    try {
      String cleanedToken = cleanToken(token);
      Claims claims = Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(cleanedToken)
              .getPayload();

      return claims.getSubject();

    } catch (ExpiredJwtException e) {
      throw new GlobalExceptionHandler.CustomException("Token expired", HttpStatus.UNAUTHORIZED);

    } catch (JwtException | IllegalArgumentException e) {
      throw new GlobalExceptionHandler.CustomException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
    }
  }

  private String cleanToken(String token) {
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    }
    return token;
  }
}