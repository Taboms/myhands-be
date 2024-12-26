package tabom.myhands.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tabom.myhands.domain.user.entity.Department;
import tabom.myhands.domain.user.entity.Role;
import tabom.myhands.domain.user.entity.User;
import tabom.myhands.domain.user.repository.DepartmentRepository;
import tabom.myhands.domain.user.repository.RoleRepository;
import tabom.myhands.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void saveAndFindById() {
        Role role = roleRepository.save(Role.builder()
                .name("사원")
                .build());

        Department department = departmentRepository.save(Department.builder()
                .name("개발팀")
                .build());

        User user = User.builder()
                .department(department)
                .role(role)
                .name("김현지")
                .email("test@example.com")
                .password("password123")
                .photo("photo.jpg")
                .dayoffCnt(10.5f)
                .employeeNum(10001)
                .joinedAt(LocalDateTime.of(2024, 12, 20, 10, 0))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getUserId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("김현지");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getRole().getName()).isEqualTo("사원");
        assertThat(foundUser.get().getDepartment().getName()).isEqualTo("개발팀");
    }
}
