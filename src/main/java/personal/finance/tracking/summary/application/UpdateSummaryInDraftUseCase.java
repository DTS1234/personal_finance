package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.application.dto.SummaryDTO;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.Summary;

import java.util.UUID;

@RequiredArgsConstructor
class UpdateSummaryInDraftUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final SummaryDTO summaryDTO;

    private final UUID userId;

    @Override
    public Summary execute() {

        Summary newUpdatedSummary = DTOMapper.from(summaryDTO);

        if (newUpdatedSummary.isInDraft()) {
            throw new IllegalStateException("Summary must be in a draft mode to be updated.");
        }

        if (InvariantsUtils.moneyValueIsInconsistent(newUpdatedSummary)) {
            throw new IllegalStateException("Invalid money value for the updated summary, "
                + "it should be equal to the sum of all items money value.");
        }

        Summary originalSummary = summaryRepository.findByIdAndUserId(newUpdatedSummary.getId().getValue(), userId);
        if (originalSummary == null) {
            throw new IllegalStateException("Summary with id " + newUpdatedSummary.getId().getValue()
                + " does not exist for this user.");
        }

        if (InvariantsUtils.summaryHasDifferentCreationDate(originalSummary, newUpdatedSummary)) {
            throw new IllegalStateException("Creation date cannot be changed for a summary.");
        }

        return summaryRepository.save(newUpdatedSummary);
    }
}
