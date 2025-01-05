package tabom.myhands.domain.dayOff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.dayOff.entity.DayOff;
import tabom.myhands.domain.user.entity.User;

import java.util.List;

public interface DayOffRepository extends JpaRepository<DayOff, Long> {
    List<DayOff> findDayOffsByUser(User user);
}
