package personal.finance.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class CreateNewSummaryUseCase implements UseCase<Summary> {

    private final UUID userId;
    private final SummaryRepository summaryRepository;

    public Summary execute() {

        validatedIfThereAreNoDrafts();

        List<Summary> confirmedSummaries =
            summaryRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId);

        if (!confirmedSummaries.isEmpty()) {
            Summary lastConfirmed = confirmedSummaries.get(0);
            List<Asset> lastConfirmedAssets = lastConfirmed.getAssets();
            List<Asset> newAssets = getNewAssets(lastConfirmedAssets);

            Summary summary = new Summary(
                null,
                userId,
                new Money(calculateMoneyValue(lastConfirmedAssets)),
                LocalDateTime.now(),
                SummaryState.DRAFT,
                newAssets
            );

            return summaryRepository.save(summary);
        }

        return summaryRepository.save(Summary.builder()
            .userId(userId)
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .assets(new ArrayList<>())
            .money(new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
            .build());
    }

    private static BigDecimal calculateMoneyValue(List<Asset> lastConfirmedAssets) {
        return lastConfirmedAssets.stream()
            .map(asset -> asset.getMoney().getMoneyValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private void validatedIfThereAreNoDrafts() {
        List<Summary> summariesInDraft = summaryRepository.findSummaryByUserIdAndState(userId,
            SummaryState.DRAFT);
        if (!summariesInDraft.isEmpty()) {
            throw new IllegalStateException("User can have only one summary in creation.");
        }
    }

    private static List<Asset> getNewAssets(List<Asset> lastConfirmedAssets) {
        return lastConfirmedAssets.stream().map(a ->
            new Asset(null,
                a.getMoney(),
                a.getName(),
                getNewItemsForAsset(a)
            )
        ).collect(Collectors.toList());
    }

    private static List<Item> getNewItemsForAsset(Asset a) {
        if (a.getItems() == null) {
            return List.of();
        }
        return a.getItems()
            .stream()
            .map(i -> new Item(null, i.getMoney(), i.getName(), i.getQuantity()))
            .collect(Collectors.toList());
    }
}
