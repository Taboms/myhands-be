package tabom.myhands.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tabom.myhands.common.config.security.TokenUtils;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.domain.auth.dto.AuthResponse;
import tabom.myhands.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseProperties responseProperties;

    @PostMapping("/retoken")
    public ResponseEntity<DtoResponse<AuthResponse>> retoken(@RequestHeader("Authorization") String refreshTokenHeader) {
        String refreshToken = TokenUtils.extractToken(refreshTokenHeader);
        AuthResponse response = authService.retoken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), response));
    }
}
