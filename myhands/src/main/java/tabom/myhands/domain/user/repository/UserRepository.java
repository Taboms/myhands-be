package tabom.myhands.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tabom.myhands.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmployeeNum(Integer employeeNum);
    Optional<User> findByUserId(Long userId);

    @Query(value = "SELECT * FROM user WHERE user_id != :userId ORDER BY name, department_id, role_id", nativeQuery = true)
    List<User> findAllExceptCurrentUser(@Param("userId") Long userId);
}
