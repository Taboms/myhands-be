package tabom.myhands.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tabom.myhands.domain.auth.dto.AuthRequest;
import tabom.myhands.domain.auth.dto.AuthResponse;
import tabom.myhands.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody AuthRequest request) {
        String newAccessToken = authService.refreshAccessToken(request);
        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.getRefreshToken()));
    }
}
