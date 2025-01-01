package tabom.myhands.error.exception;

import lombok.Getter;
import tabom.myhands.error.errorcode.ErrorCode;

@Getter
public class ScheduleApiException extends RuntimeException{
    private final ErrorCode errorCode;
    public ScheduleApiException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}