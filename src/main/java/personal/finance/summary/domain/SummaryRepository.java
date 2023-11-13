package personal.finance.summary.domain;

import java.util.List;

public interface SummaryRepository {

    Summary save(Summary summary);

    List<Summary> saveAll(List<Summary> entityList);

    Summary findById(SummaryId id);

    Summary findByIdAndUserId(Long summaryId, Long userId);

    void deleteAll();

    List<Summary> findSummaryByStateEqualsOrderById(SummaryState summaryState);

    List<Summary> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState);

    List<Summary> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, Long userId);
}
