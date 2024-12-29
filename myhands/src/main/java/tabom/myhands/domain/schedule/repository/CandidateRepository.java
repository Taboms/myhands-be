package tabom.myhands.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tabom.myhands.domain.schedule.entity.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}

