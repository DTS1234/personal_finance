package personal.finance.tracking.summary.domain;

import java.util.List;
import java.util.UUID;

public interface SummaryRepository {

    Summary save(Summary summary);

    List<Summary> saveAll(List<Summary> entityList);

    Summary findById(UUID summaryId);

    Summary findByIdAndUserId(UUID summaryId, UUID userId);

    List<Summary> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, UUID userId);

    List<Summary> findSummaryByUserIdAndState(UUID userId, SummaryState summaryState);
}
