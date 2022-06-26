package personal.finance.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetRepository;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

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
}
