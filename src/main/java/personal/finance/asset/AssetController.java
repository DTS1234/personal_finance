package personal.finance.asset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.summary.SummaryMapper;
import personal.finance.summary.SummaryService;
import personal.finance.summary.SummaryState;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Slf4j
public class AssetController {

    private final AssetRepository assetRepository;
    private final AssetService assetService;
    private final SummaryRepository summaryRepository;

    private final SummaryService summaryService;

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return SummaryMapper.toSummary(summaryService.getLatestConfirmedSummary()).getAssets();
    }

    @GetMapping("/assets/sum")
    public double sumAssetMoneyValue() {
        return summaryService.getLatestConfirmedSummary().getAssets().stream().map(Asset::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
    }

    @GetMapping("/assets/percentages")
    public Map<Long, Double> getPercentages() {
        double sum = summaryService.getLatestConfirmedSummary().getAssets().stream().map(Asset::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add)
            .doubleValue();

        List<Asset> all = summaryService.getLatestConfirmedSummary().getAssets();

        Map<Long, Double> result = new HashMap<>();
        all.forEach(asset ->
            result.put(asset.getId(), calculatePercentage(sum, asset))
        );

        return result;
    }

    private double calculatePercentage(double sum, Asset asset) {
        return asset.getMoneyValue()
            .divide(
                BigDecimal.valueOf(sum), 4, RoundingMode.HALF_UP
            )
            .multiply(
                BigDecimal.valueOf(100)
            )
            .doubleValue();
    }

}
