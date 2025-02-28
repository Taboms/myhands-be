package tabom.myhands.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode{
    INVALID_AUTH_HEADER(3000, HttpStatus.BAD_REQUEST, "The Authorization header is missing or invalid"),
    INVALID_TOKEN(3001, HttpStatus.UNAUTHORIZED, "Invalid token"),
    EXPIRED_TOKEN(3002, HttpStatus.UNAUTHORIZED, "The token has expired"),
    REFRESH_TOKEN_MISMATCH(3003, HttpStatus.UNAUTHORIZED, "The refresh token does not match with the stored token"),
    TOKEN_BLACKLISTED(3004, HttpStatus.UNAUTHORIZED, "The token is blacklisted");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
