package tabom.myhands.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.schedule.entity.Candidate;
import tabom.myhands.domain.schedule.entity.Schedule;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findBySchedule(Schedule schedule);
}
