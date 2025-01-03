package tabom.myhands.domain.dayOff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.dayOff.entity.HalfOff;
import tabom.myhands.domain.user.entity.User;

import java.util.List;

public interface HalfOffRepository extends JpaRepository<HalfOff, Long> {
    List<HalfOff> findHalfOffsByUser(User user);
}
