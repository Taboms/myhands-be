package tabom.myhands.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.schedule.entity.Schedule;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByScheduleId(Long scheduleId);
}
