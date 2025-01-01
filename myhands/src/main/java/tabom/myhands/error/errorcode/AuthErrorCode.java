package tabom.myhands.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode{
    INVALID_AUTH_HEADER(3000, HttpStatus.BAD_REQUEST, "The Authorization header is missing or invalid"),
    INVALID_REFRESH_TOKEN(3001, HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    EXPIRED_REFRESH_TOKEN(3002, HttpStatus.UNAUTHORIZED, "The refresh token has expired"),
    REFRESH_TOKEN_MISMATCH(3003, HttpStatus.UNAUTHORIZED, "The refresh token does not match with the stored token");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
