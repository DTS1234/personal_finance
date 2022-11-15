package personal.finance.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetMapper;
import personal.finance.asset.AssetRepository;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.action.Action;
import personal.finance.summary.domain.action.AddAssetDraftAction;
import personal.finance.summary.domain.action.ChangeAssetDraftAction;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SummaryService {

    private final AssetRepository assetRepository;
    private final SummaryRepository summaryRepository;

    public List<Asset> getAvailableAssets(Long summaryId) {
        return assetRepository.findAll();
    }

    public List<Summary> getAllConfirmedSummaries() {
        return summaryRepository.findSummaryByStateEqualsOrderById(SummaryState.CONFIRMED).stream().map(this::mapToSummary).collect(Collectors.toList());
    }

    Summary mapToSummary(SummaryEntity summaryEntity) {
        return Summary.builder()
                .id(summaryEntity.getId())
                .moneyValue(summaryEntity.getMoneyValue())
                .date(summaryEntity.getDate())
                .state(summaryEntity.getState())
                .build();
    }

    public Summary createNewSummary(Summary newSummary) {
        newSummary.setState(SummaryState.DRAFT);
        newSummary.setAssets(new ArrayList<>());
        newSummary.setMoneyValue(BigDecimal.valueOf(0));

        SummaryEntity summaryEntity = SummaryMapper.toEntity(newSummary);
        SummaryEntity save = summaryRepository.save(summaryEntity);

        return SummaryMapper.toSummary(save);
    }

    public Summary addAsset(Long id, Asset asset) {
        Action action = new AddAssetDraftAction(new PersistenceAdapter(summaryRepository, assetRepository));
        SummaryEntity summaryEntity = summaryRepository.findById(id).orElseThrow();
        SummaryDomain summaryDomain = SummaryMapper.toDomain(summaryEntity);
        AssetDomain assetDomain = AssetMapper.toDomain(asset);
        SummaryDomain domain = action.execute(assetDomain, summaryDomain);
        return SummaryMapper.toSummary(domain);
    }

    public Summary confirmSummary(Long id) {
        SummaryEntity summaryEntity = summaryRepository.findById(id).orElseThrow();

        if (summaryEntity.getAssets().isEmpty()) {
            throw new IllegalArgumentException("You cannot confirm empty summary.");
        }

        if (summaryEntity.getState().compareTo(SummaryState.DRAFT) != 0) {
            throw new IllegalStateException(String.format("You cannot confirm summary in that state: %s", summaryEntity.getState()));
        }

        BigDecimal sumOfAssetMoneyValue = summaryEntity.getAssets().stream().map(Asset::getMoneyValue).reduce(BigDecimal.valueOf(0), BigDecimal::add).setScale(1, RoundingMode.HALF_UP);
        BigDecimal bigDecimalSummaryValue = summaryEntity.getMoneyValue().setScale(1, RoundingMode.HALF_UP);

        if (bigDecimalSummaryValue.compareTo(sumOfAssetMoneyValue)!=0) {
            throw new IllegalStateException(String.format("Money value for summary should be equal to sum of money value of all assets. %s != %s", bigDecimalSummaryValue, sumOfAssetMoneyValue));
        }

        SummaryDomain summaryDomain = SummaryMapper.toDomain(summaryEntity);
        summaryDomain.changeState(SummaryState.CONFIRMED);

        return SummaryMapper.toSummary(summaryRepository.save(SummaryMapper.toEntity(summaryDomain)));
    }

    public Summary editAsset(Long summaryId, Long assetId, Asset editedAsset) {
        Action editAssetAction = new ChangeAssetDraftAction(new PersistenceAdapter(summaryRepository, assetRepository));
        SummaryEntity summaryEntity = summaryRepository.findById(summaryId).orElseThrow();
        SummaryDomain summaryDomain = editAssetAction.execute(AssetMapper.toDomain(editedAsset), SummaryMapper.toDomain(summaryEntity));
        return SummaryMapper.toSummary(summaryDomain);
    }
}
