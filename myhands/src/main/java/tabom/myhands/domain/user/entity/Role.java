package tabom.myhands.domain.user.entity;

import tabom.myhands.error.errorcode.UserErrorCode;
import tabom.myhands.error.exception.UserApiException;

import java.util.Arrays;

public enum Role {
    CEO(1),
    EXECUTIVE(2),
    DIRECTOR(3),
    MANAGER(4),
    ASSISTANT_MANAGER(5),
    STAFF(6),
    INTERN(7);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new UserApiException(UserErrorCode.INVALID_ROLE_VALUE));
    }
}
