package tabom.myhands.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tabom.myhands.common.config.security.JwtTokenProvider;
import tabom.myhands.domain.auth.dto.AuthRequest;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;
import tabom.myhands.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Override
    public String refreshAccessToken(AuthRequest request) {
        String storedToken = redisService.getRefreshToken(request.getUserId());

        userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new UserApiException(UserErrorCode.USER_ID_NOT_FOUND));

        if (storedToken == null || !storedToken.equals(request.getRefreshToken())) {
            throw new UserApiException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtTokenProvider.generateAccessToken(request.getUserId());
    }
}
