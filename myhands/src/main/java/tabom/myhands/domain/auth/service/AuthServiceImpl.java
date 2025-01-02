package tabom.myhands.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tabom.myhands.common.config.security.JwtTokenProvider;
import tabom.myhands.domain.auth.dto.AuthResponse;
import tabom.myhands.error.errorcode.AuthErrorCode;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.AuthApiException;
import tabom.myhands.error.exception.UserApiException;
import tabom.myhands.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Override
    public AuthResponse retoken(Long userId, String refreshToken) {
        String storedToken = redisService.getRefreshToken(userId);

        if (storedToken == null) {
            throw new AuthApiException(AuthErrorCode.INVALID_TOKEN);
        }

        if (!storedToken.equals(refreshToken)) {
            throw new AuthApiException(AuthErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        redisService.saveRefreshToken(userId, newRefreshToken, jwtTokenProvider.getRefreshTokenExpireTime());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
