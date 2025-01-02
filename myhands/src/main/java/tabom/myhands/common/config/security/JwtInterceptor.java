package tabom.myhands.common.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tabom.myhands.domain.user.repository.UserRepository;
import tabom.myhands.error.errorcode.AuthErrorCode;
import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.AuthApiException;
import tabom.myhands.error.exception.UserApiException;

@Component
@Slf4j
@RequiredArgsConstructor

public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthApiException(AuthErrorCode.INVALID_AUTH_HEADER);
        }

        String jwt = authorizationHeader.substring(7);
        try {
            if (!jwtTokenProvider.validateToken(jwt)) {
                throw new AuthApiException(AuthErrorCode.INVALID_TOKEN);
            }

            Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

            if (!userRepository.existsById(userId)) {
                throw new UserApiException(UserErrorCode.USER_ID_NOT_FOUND);
            }

            request.setAttribute("userId", userId);
            return true;
        } catch (AuthApiException e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
            throw e;
        }
    }
}
