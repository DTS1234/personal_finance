package personal.finance.summary.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SummaryJpaRepository extends JpaRepository<SummaryEntity, UUID>,
    JpaSpecificationExecutor<SummaryEntity> {

    List<SummaryEntity> findSummaryByStateEqualsOrderById(SummaryState summaryState);

    List<SummaryEntity> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState);

    List<SummaryEntity> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, UUID userId);

    Optional<SummaryEntity> findByIdAndUserId(UUID summaryId, UUID userId);

    Set<SummaryEntity> findSummaryByUserIdAndStateEquals(UUID userId, SummaryState summaryState);
}
