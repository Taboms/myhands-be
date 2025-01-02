package tabom.myhands.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tabom.myhands.domain.user.entity.User;

public class UserResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class login{
        private String accessToken;
        private String refreshToken;
        private Long userId;
        private String name;
        private String photo;
        private String role;
        private String department;

        public static UserResponse.login build(String accessToken, String refreshToken, User user) {
            return login.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getUserId())
                    .name(user.getName())
                    .photo(user.getPhoto())
                    .role(user.getRole().getName())
                    .department(user.getDepartment().getName())
                    .build();
        }
    }
}
