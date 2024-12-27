package tabom.myhands.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    EMAIL_ALREADY_EXISTS(2001, HttpStatus.BAD_REQUEST, "Email is already in use"),
    INVALID_ROLE_VALUE(2002, HttpStatus.BAD_REQUEST, "Invalid Role value"),
    INVALID_DEPARTMENT_VALUE(2003, HttpStatus.BAD_REQUEST, "Invalid Department value"),
    IMAGE_UPLOAD_FAILED(2004, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload photo"),
    EMPLOYEE_NUM_ALREADY_EXISTS(2005, HttpStatus.BAD_REQUEST, "Employee number is already in use");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
