package tabom.myhands.domain.dayOff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.dayOff.entity.DayOff;
import tabom.myhands.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface DayOffRepository extends JpaRepository<DayOff, Long> {
    Optional<DayOff> findByDayOffId(Long dayOffId);

    List<DayOff> findDayOffsByUser(User user);
}
