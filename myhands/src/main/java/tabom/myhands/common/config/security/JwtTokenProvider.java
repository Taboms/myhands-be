package tabom.myhands.common.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tabom.myhands.common.properties.JwtProperties;
import tabom.myhands.domain.auth.service.RedisService;
import tabom.myhands.error.errorcode.AuthErrorCode;
import tabom.myhands.error.exception.AuthApiException;

import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisService redisService;
    private final JwtProperties jwtProperties;
    private String secretKey;

    @PostConstruct
    protected void init() {
        String secret = jwtProperties.getSecretKey();
        this.secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtProperties.getAccessTokenExpireTime());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtProperties.getRefreshTokenExpireTime());

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        redisService.saveRefreshToken(userId, refreshToken, jwtProperties.getRefreshTokenExpireTime());
        return refreshToken;
    }

    public Long validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.valueOf(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new AuthApiException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new AuthApiException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public long getAccessTokenExpireTime() {
        return jwtProperties.getAccessTokenExpireTime();
    }

    public long getRefreshTokenExpireTime() {
        return jwtProperties.getRefreshTokenExpireTime();
    }
}
