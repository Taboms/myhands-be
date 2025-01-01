package tabom.myhands.domain.auth.service;

import tabom.myhands.domain.auth.dto.AuthRequest;

public interface AuthService {
    String refreshAccessToken(AuthRequest request);
}
