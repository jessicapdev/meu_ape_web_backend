package com.br.meu_ape.config;

import com.br.meu_ape.exception.GlobalExceptionHandler;
import com.br.meu_ape.model.UsuarioRole;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key}")
  private String secretKeyString;

  @Value("${security.jwt.token.expire-length}")
  private long validityInMilliseconds;

  @Autowired
  private MyUserDetails myUserDetails;

  private SecretKey secretKey;

  @PostConstruct
  protected void init() {
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(String username, List<UsuarioRole> usuarioRoles) {

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
            .subject(username)
            .claim("auth", usuarioRoles.stream()
                    .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()))
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey)
            .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    String cleanedToken = cleanToken(token);
    return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(cleanedToken)
            .getPayload()
            .getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return cleanToken(bearerToken);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      String cleanedToken = cleanToken(token);
      Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(cleanedToken);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new GlobalExceptionHandler.CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Remove palavra Bearer e espaços em branco e caracteres de controle do token
   */
  private String cleanToken(String token) {
    if (token == null || token.isBlank()) {
      return null;
    }
    return token.replaceFirst("(?i)^Bearer\\s*", "").trim();
  }
}