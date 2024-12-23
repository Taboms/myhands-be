package tabom.myhands.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import tabom.myhands.domain.user.entity.Role;

public class UserRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Join {
        private Integer departmentId;
        private String name;
        private String email;
        private String password;
        private String photo;
        private Float dayoffCnt;
        private Role role;
        private Integer employeeNum;
        private LocalDateTime joinedAt;
    }
}
