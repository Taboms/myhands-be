package tabom.myhands.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {
    Schedule_ID_NOT_FOUND(1000, HttpStatus.NOT_FOUND, "Schedule not found with the given ID"),
    INVALID_VALUE(1001, HttpStatus.BAD_REQUEST, "Invalid value");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
