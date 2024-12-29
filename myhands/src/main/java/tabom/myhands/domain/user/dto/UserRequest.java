package tabom.myhands.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join {
        private Integer departmentId;
        private String name;
        private String email;
        private String password;
        private Float dayoffCnt;
        private Integer role;
        private Integer employeeNum;
        private LocalDateTime joinedAt;
    }
}
