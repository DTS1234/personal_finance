package personal.finance.asset;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Service
@AllArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public Map<Long, Double> getPercentages() {
        List<Asset> all = assetRepository.findAll();
        double sum = assetRepository.selectAssetMoneyValueSum();
        Map<Long, Double> result = new HashMap<>();
        all.forEach(asset ->
                result.put(asset.getId(), calculatePercentage(sum, asset))
        );
        return result;
    }

    private double calculatePercentage(double sum, Asset asset) {
        return asset.getMoneyValue()
                .divide(
                        BigDecimal.valueOf(sum), 4, RoundingMode.HALF_EVEN
                )
                .multiply(
                        BigDecimal.valueOf(100)
                )
                .doubleValue();
    }
}
