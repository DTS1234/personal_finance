package personal.finance.summary.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.summary.domain.model.Summary;
import personal.finance.summary.domain.model.SummaryState;

import java.util.List;

public interface SummaryJpaRepository extends JpaRepository<Summary, Long> {

    List<Summary> findSummaryByStateEqualsOrderById(SummaryState summaryState);

    List<Summary> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState);
}
