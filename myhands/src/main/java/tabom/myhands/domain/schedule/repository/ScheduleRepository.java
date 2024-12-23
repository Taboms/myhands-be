package tabom.myhands.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.schedule.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
