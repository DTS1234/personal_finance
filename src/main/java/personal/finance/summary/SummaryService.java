package personal.finance.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetMapper;
import personal.finance.asset.AssetRepository;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.action.Action;
import personal.finance.summary.domain.action.ChangeAssetDraftAction;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
public class SummaryService {

    private final AssetRepository assetRepository;
    private final SummaryRepository summaryRepository;

    public List<Asset> getAvailableAssets(Long summaryId) {
        return summaryRepository.findById(summaryId).orElseThrow().getAssets();
    }

    public List<Summary> getAllConfirmedSummaries() {
        return summaryRepository.findSummaryByStateEqualsOrderById(SummaryState.CONFIRMED).stream().map(this::mapToSummary).collect(toList());
    }

    Summary mapToSummary(SummaryEntity summaryEntity) {
        return Summary.builder()
            .id(summaryEntity.getId())
            .moneyValue(summaryEntity.getMoneyValue())
            .date(summaryEntity.getDate())
            .state(summaryEntity.getState())
            .assets(summaryEntity.getAssets())
            .build();
    }

    public Summary createNewSummary(Summary newSummary) {

        Summary summary = SummaryMapper.toSummary(getLatestConfirmedSummary());

        List<Asset> newAssets = summary.getAssets().stream().map(
            a -> new Asset(null, a.getMoneyValue(), a.getName(), Collections.emptyList())
        ).toList();

        newSummary.setState(SummaryState.DRAFT);
        newSummary.setAssets(newAssets);
        newSummary.setMoneyValue(BigDecimal.valueOf(0L));
        newSummary.setDate(LocalDateTime.now());

        SummaryEntity summaryEntity = SummaryMapper.toEntity(newSummary);
        SummaryEntity save = summaryRepository.save(summaryEntity);

        return SummaryMapper.toSummary(save);
    }

    public SummaryEntity getLatestConfirmedSummary() {
        return summaryRepository.findSummaryByStateEqualsOrderByDateDesc(SummaryState.CONFIRMED).get(0);
    }

    public Summary updateSummaryInDraft(Summary summary) {

        if (summary.getState().compareTo(SummaryState.DRAFT) != 0) {
            throw new IllegalStateException("You can only edit draft summaries!");
        }

        SummaryEntity entity = SummaryMapper.toEntity(summary);
        SummaryEntity summaryEntity = summaryRepository.save(entity);
        return SummaryMapper.toSummary(SummaryMapper.toDomain(summaryEntity));
    }

    // public Summary addAsset(Long id, Asset asset) {
    //     Action action = new AddAssetDraftAction(new PersistenceAdapter(summaryRepository, assetRepository));
    //     SummaryEntity summaryEntity = summaryRepository.findById(id).orElseThrow();
    //     SummaryDomain summaryDomain = SummaryMapper.toDomain(summaryEntity);
    //     AssetDomain assetDomain = AssetMapper.toDomain(asset);
    //     SummaryDomain domain = action.execute(assetDomain, summaryDomain);
    //     return SummaryMapper.toSummary(domain);
    // }

    public Summary confirmSummary(Long id) {
        SummaryEntity summaryEntity = summaryRepository.findById(id).orElseThrow();

        if (summaryEntity.getAssets().isEmpty()) {
            throw new IllegalArgumentException("You cannot confirm an empty summary.");
        }

        if (summaryEntity.getState().compareTo(SummaryState.DRAFT) != 0) {
            throw new IllegalStateException(String.format("You cannot confirm a summary in that state: %s", summaryEntity.getState()));
        }

        BigDecimal sumOfAssetMoneyValue = summaryEntity.getAssets().stream().map(Asset::getMoneyValue).reduce(BigDecimal.valueOf(0), BigDecimal::add)
            .setScale(1, RoundingMode.HALF_UP);
        SummaryDomain summaryDomain = SummaryMapper.toDomain(summaryEntity);
        summaryDomain.setMoneyValue(sumOfAssetMoneyValue);
        summaryDomain.changeState(SummaryState.CONFIRMED);

        return SummaryMapper.toSummary(summaryRepository.save(SummaryMapper.toEntity(summaryDomain)));
    }

    public Summary updateSummaryAssetInDraft(Long summaryId, Long assetId, Asset asset) {
        SummaryEntity summaryEntity = summaryRepository.findById(summaryId).orElseThrow();

        Asset assetToUpdate = summaryEntity.getAssets().stream().filter(a -> a.getId().equals(assetId)).findFirst().orElseThrow();
        summaryEntity.getAssets().remove(assetToUpdate);
        summaryEntity.addAsset(asset);
        SummaryEntity updated = summaryRepository.save(summaryEntity);

        return SummaryMapper.toSummary(updated);
    }

    public Summary getSummary(long parseLong) {
        SummaryEntity summaryEntity = summaryRepository.findById(parseLong).orElseThrow();
        return SummaryMapper.toSummary(summaryEntity);
    }
}
