package tabom.myhands.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DayOffErrorCode implements ErrorCode {
    REQUIRED_VALUE_IS_NULL(4001, HttpStatus.BAD_REQUEST, "Required value is null"),
    INVALID_OFF_TYPE(4002, HttpStatus.BAD_REQUEST, "The offType must be FULL or HALF"),
    INVALID_DATE_RANGE(4003, HttpStatus.BAD_REQUEST, "Input date (range) is invalid"),
    DUPLICATE_DAY_OFF_REQUEST(4004, HttpStatus.CONFLICT, "A day off request already exists for this date"),
    PERIOD_LIMIT_EXCEEDED(4005, HttpStatus.BAD_REQUEST, "The requested day off period is too long");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
