package tabom.myhands.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DayOffErrorCode implements ErrorCode {
    USER_ID_NOT_FOUND(4001, HttpStatus.NOT_FOUND, "User not found with the given ID"),
    INVALID_DATE_RANGE(4002, HttpStatus.BAD_REQUEST, "The start {date/time} cannot be after the end {date/time}"),
    DUPLICATE_DAY_OFF_REQUEST(4003, HttpStatus.CONFLICT, "A day off request already exists for this date"),
    PERIOD_LIMIT_EXCEEDED(4004, HttpStatus.BAD_REQUEST, "The requested day off period is too long"),
    INVALID_OFF_TYPE(4005, HttpStatus.BAD_REQUEST, "The offType must be FULL or HALF");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
