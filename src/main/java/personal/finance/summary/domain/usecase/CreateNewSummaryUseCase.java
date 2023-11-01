package personal.finance.summary.domain.usecase;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.model.Asset;
import personal.finance.summary.domain.model.Item;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.model.Summary;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateNewSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;

    public Summary execute() {
        List<Summary> confirmedSummaries = summaryRepository.findSummaryByStateEqualsOrderByDateDesc(SummaryState.CONFIRMED);

        if (!confirmedSummaries.isEmpty()) {
            Summary lastConfirmed = confirmedSummaries.get(0);
            List<Asset> lastConfirmedAssets = lastConfirmed.getAssets();

            BigDecimal moneyValue = lastConfirmedAssets.stream().map(Asset::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<Asset> newAssets = getNewAssets(lastConfirmedAssets);

            Summary summary = new Summary(
                null,
                moneyValue,
                LocalDateTime.now(),
                SummaryState.DRAFT,
                newAssets
            );

            return summaryRepository.save(summary);
        }

        return summaryRepository.save(Summary.builder()
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .assets(new ArrayList<>())
            .moneyValue(BigDecimal.ZERO)
            .build());
    }

    private static List<Asset> getNewAssets(List<Asset> lastConfirmedAssets) {
        return lastConfirmedAssets.stream().map(a ->
            new Asset(null,
                a.getMoneyValue(),
                a.getName(),
                getNewItemsForAsset(a)
            )
        ).collect(Collectors.toList());
    }

    private static List<Item> getNewItemsForAsset(Asset a) {
        if (a.getItems() == null) {
            return List.of();
        }
        return a.getItems().stream().map(i -> new Item(null, i.getMoneyValue(), i.getName(), i.getQuantity())).collect(Collectors.toList());
    }

}
