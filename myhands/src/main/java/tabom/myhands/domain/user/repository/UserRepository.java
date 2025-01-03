package tabom.myhands.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmployeeNum(Integer employeeNum);
    Optional<User> findByUserId(Long userId);
}
