package personal.finance.tracking.summary.application;

import personal.finance.common.UseCase;
import personal.finance.tracking.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.SummaryState;

import java.util.List;
import java.util.UUID;

public class GetCurrentDraftUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final UUID userId;

    public GetCurrentDraftUseCase(SummaryRepository summaryRepository, UUID userId) {
        this.summaryRepository = summaryRepository;
        this.userId = userId;
    }

    @Override
    public Summary execute() {
        List<Summary> summary = summaryRepository.findSummaryByUserIdAndState(userId, SummaryState.DRAFT);

        if (summary.isEmpty()) {
            throw new NoSummaryInDraftException("No summary is being created for this user.");
        }

        return summary.get(0);
    }
}
