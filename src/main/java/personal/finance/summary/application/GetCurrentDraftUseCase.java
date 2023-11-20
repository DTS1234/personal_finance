package personal.finance.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetCurrentDraftUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final UUID userId;

    @Override
    public Summary execute() {
        List<Summary> summary = summaryRepository.findSummaryByUserIdAndState(userId, SummaryState.DRAFT);

        if (summary.isEmpty()) {
            throw new NoSummaryInDraftException("No summary is being created for this user.");
        }

        return summary.get(0);
    }
}
