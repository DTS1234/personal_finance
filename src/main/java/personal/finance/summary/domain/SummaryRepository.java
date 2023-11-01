package personal.finance.summary.domain;

import personal.finance.summary.domain.model.SummaryState;
import personal.finance.summary.domain.model.Summary;

import java.util.List;
import java.util.Optional;

public interface SummaryRepository {

    Summary save(Summary summary);

    List<Summary> saveAll(List<Summary> entityList);

    Optional<Summary> findById(Long id);

    void deleteAll();

    List<Summary> findSummaryByStateEqualsOrderById(SummaryState summaryState);

    List<Summary> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState);
}
