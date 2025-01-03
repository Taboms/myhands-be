package tabom.myhands.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tabom.myhands.domain.user.dto.UserRequest;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(30)")
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String password;

    private String photo;

    @Column(name = "dayoff_cnt")
    private Float dayoffCnt;

    @Column(name = "employee_num", unique = true, nullable = false)
    private Integer employeeNum;

    @Column(name = "joined_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static User addUser(UserRequest.Join request, Department department, Role role) {
        return User.builder()
                .department(department)
                .role(role)
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .dayoffCnt(request.getDayoffCnt())
                .employeeNum(request.getEmployeeNum())
                .joinedAt(request.getJoinedAt())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updatePhoto(String photoUrl) {
        this.photo = photoUrl;
    }
}
