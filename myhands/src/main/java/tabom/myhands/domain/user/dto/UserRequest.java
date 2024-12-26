package tabom.myhands.domain.user.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tabom.myhands.domain.user.entity.Role;

public class UserRequest {

    @Getter
    @Builder
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
