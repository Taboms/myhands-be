package tabom.myhands.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.user.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}