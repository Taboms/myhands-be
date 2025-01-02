package tabom.myhands.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tabom.myhands.common.config.security.TokenUtils;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.domain.auth.dto.AuthResponse;
import tabom.myhands.domain.auth.service.AuthService;
import tabom.myhands.domain.auth.service.RedisService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseProperties responseProperties;
    private final RedisService redisService;

    @PostMapping("/retoken")
    public ResponseEntity<DtoResponse<AuthResponse>> retoken(HttpServletRequest request, @RequestHeader("Authorization") String refreshTokenHeader) {
        Long userId = (Long) request.getAttribute("userId"); // request에서 추출한 userId
        String refreshToken = TokenUtils.extractToken(refreshTokenHeader); // 레디스에 저장된 리프레시와 일치하는지 검증하기 위함
        AuthResponse response = authService.retoken(userId, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), response));
    }
}
