package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.common.events.EventPublisher;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.domain.events.SummaryCreated;

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
    private final EventPublisher eventPublisher;

    public Summary execute() {
        validatedIfThereAreNoDrafts();

        List<Summary> confirmedSummaries =
            summaryRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId);
        SummaryId summaryId = SummaryId.random();

        if (!confirmedSummaries.isEmpty()) {
            Summary lastConfirmed = confirmedSummaries.get(0);
            List<Asset> lastConfirmedAssets = lastConfirmed.getAssets();
            List<Asset> newAssets = getNewAssets(lastConfirmedAssets);

            Summary summary = new Summary(
                summaryId,
                userId,
                new Money(calculateMoneyValue(lastConfirmedAssets), lastConfirmed.getMoney().getCurrency()),
                LocalDateTime.now(),
                SummaryState.DRAFT,
                newAssets,
                newAssets.stream().map(Asset::getIdValue).collect(Collectors.toSet())
            );

            eventPublisher.publishEvent(new SummaryCreated(summaryId.getValue(), lastConfirmed.getIdValue(), UUID.randomUUID()));

            return summaryRepository.save(summary);
        }

        Summary summarySaved = summaryRepository.save(Summary.builder()
            .id(summaryId)
            .userId(userId)
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .assets(new ArrayList<>())
            .money(new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
            .build());

        eventPublisher.publishEvent(new SummaryCreated(summarySaved.getIdValue(), null, UUID.randomUUID()));
        return summarySaved;
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
            new Asset(AssetId.random(),
                a.getMoney(),
                a.getName(),
                getNewItemsForAsset(a),
                a.getType(),
                a.getSummaryId()
            )
        ).collect(Collectors.toList());
    }

    private static List<Item> getNewItemsForAsset(Asset a) {
        if (a.getItems() == null) {
            return List.of();
        }
        return a.getItems()
            .stream()
            .map(i -> new Item(ItemId.random(), i.getMoney(), i.getName(), i.getQuantity()))
            .collect(Collectors.toList());
    }
}
