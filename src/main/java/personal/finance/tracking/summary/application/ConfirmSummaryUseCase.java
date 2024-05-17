package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import personal.finance.common.UseCase;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.Summary;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
class ConfirmSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final UUID summaryId;

    private final UUID userId;

    @Override
    public Summary execute() {

        Summary summaryFound = summaryRepository.findByIdAndUserId(summaryId, userId);
        if (summaryFound == null) {
            throw new IllegalStateException("Summary with id of " + summaryId + " does not exist for this user.");
        }

        if (summaryFound.isInDraft()) {
            throw new IllegalStateException("Summary can be confirmed only if it is in DRAFT state.");
        }

        Summary unsavedConfirmed = summaryFound.confirm();
        return summaryRepository.save(unsavedConfirmed);
    }
}
