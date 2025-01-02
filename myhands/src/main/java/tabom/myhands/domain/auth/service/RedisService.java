package tabom.myhands.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set(getKey(userId), refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    public void addToBlacklist(String accessToken, long expirationTime) {
        redisTemplate.opsForValue().set(getBlacklistKey(accessToken), "true", expirationTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(Long userId) {
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(getKey(userId));
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(getBlacklistKey(accessToken));
    }

    private String getKey(Long userId) {
        return "refreshToken:" + userId;
    }

    private String getBlacklistKey(String token) {
        return "blacklist:" + token;
    }
}
