package tabom.myhands.error.exception;

import lombok.Getter;
import tabom.myhands.error.errorcode.ErrorCode;

@Getter
public class DayOffApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public DayOffApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
