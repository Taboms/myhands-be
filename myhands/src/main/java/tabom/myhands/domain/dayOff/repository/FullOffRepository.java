package tabom.myhands.domain.dayOff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.dayOff.entity.FullOff;
import tabom.myhands.domain.user.entity.User;

import java.util.List;

public interface FullOffRepository extends JpaRepository<FullOff, Long> {
    List<FullOff> findFullOffsByUser(User user);
}
