package personal.finance.summary.application;

import personal.finance.common.UseCase;
import personal.finance.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.UserRepository;

import java.util.List;
import java.util.UUID;

public class GetCurrentDraftUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final UUID userId;

    private final UserRepository userRepository;

    public GetCurrentDraftUseCase(SummaryRepository summaryRepository, UUID userId, UserRepository userRepository) {
        this.summaryRepository = summaryRepository;
        this.userId = userId;
        this.userRepository = userRepository;
    }

    @Override
    public Summary execute() {
        Currency currencySavedForUser = userRepository.getCurrency(userId);
        List<Summary> summary = summaryRepository.findSummaryByUserIdAndState(userId, SummaryState.DRAFT);

        if (summary.isEmpty()) {
            throw new NoSummaryInDraftException("No summary is being created for this user.");
        }

        Summary summaryFound = summary.get(0);
        Currency currencyFound = summaryFound.getMoney().getCurrency();
        if (currencySavedForUser != currencyFound) {
            summaryFound.convertCurrencyTo(currencySavedForUser);
        }

        return summaryFound;
    }
}
