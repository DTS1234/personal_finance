package personal.finance.summary.usecase;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.model.AssetEntity;
import personal.finance.summary.domain.model.ItemEntity;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.model.SummaryEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateNewSummaryUseCase implements UseCase<SummaryEntity> {

    private final SummaryRepository summaryRepository;

    public SummaryEntity execute() {
        List<SummaryEntity> confirmedSummaries = summaryRepository.findSummaryByStateEqualsOrderByDateDesc(SummaryState.CONFIRMED);

        if (!confirmedSummaries.isEmpty()) {
            SummaryEntity lastConfirmed = confirmedSummaries.get(0);
            List<AssetEntity> lastConfirmedAssets = lastConfirmed.getAssets();

            BigDecimal moneyValue = lastConfirmedAssets.stream().map(AssetEntity::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<AssetEntity> newAssets = getNewAssets(lastConfirmedAssets);

            SummaryEntity summaryEntity = new SummaryEntity(
                null,
                moneyValue,
                LocalDateTime.now(),
                SummaryState.DRAFT,
                newAssets
            );

            return summaryRepository.save(summaryEntity);
        }

        return summaryRepository.save(SummaryEntity.builder()
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .assets(new ArrayList<>())
            .moneyValue(BigDecimal.ZERO)
            .build());
    }

    private static List<AssetEntity> getNewAssets(List<AssetEntity> lastConfirmedAssets) {
        return lastConfirmedAssets.stream().map(a ->
            new AssetEntity(null,
                a.getMoneyValue(),
                a.getName(),
                getNewItemsForAsset(a)
            )
        ).collect(Collectors.toList());
    }

    private static List<ItemEntity> getNewItemsForAsset(AssetEntity a) {
        if (a.getItems() == null) {
            return List.of();
        }
        return a.getItems().stream().map(i -> new ItemEntity(null, i.getMoneyValue(), i.getName(), i.getQuantity())).collect(Collectors.toList());
    }

}
