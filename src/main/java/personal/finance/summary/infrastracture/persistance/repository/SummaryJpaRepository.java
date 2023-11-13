package personal.finance.summary.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.List;
import java.util.Optional;

public interface SummaryJpaRepository extends JpaRepository<SummaryEntity, Long> {

    List<SummaryEntity> findSummaryByStateEqualsOrderById(SummaryState summaryState);

    List<SummaryEntity> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState);

    List<SummaryEntity> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, Long userId);

    Optional<SummaryEntity> findByIdAndUserId(Long summaryId, Long userId);
}
