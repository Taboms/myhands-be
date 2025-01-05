package tabom.myhands.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tabom.myhands.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login{
        private String accessToken;
        private String refreshToken;
        private Long userId;
        private String name;
        private String photo;
        private String role;
        private String department;

        public static UserResponse.Login build(String accessToken, String refreshToken, User user) {
            return Login.builder()
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserList {
        private List<UserItem> userList;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UserItem {
            private Long userId;
            private String name;
            private String role;
            private String department;
            private String photo;

            @JsonInclude(JsonInclude.Include.NON_NULL) // null 값일 경우 필드 제외
            private String phone;

            public static UserItem build(User user, boolean includePhone) {
                return UserItem.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .role(user.getRole().getName())
                        .department(user.getDepartment().getName())
                        .photo(user.getPhoto())
                        .phone(includePhone ? user.getPhone() : null)
                        .build();
            }
        }

        public static UserList listBuild(List<User> users, boolean includePhone) {
            return UserList.builder()
                    .userList(users.stream()
                            .map(user -> UserItem.build(user, includePhone))
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
