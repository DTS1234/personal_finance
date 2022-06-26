package acceptance;

import personal.finance.asset.Asset;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
public class GivenAssets {

    public static List<Asset> assets() {
        return Arrays.asList(
                Asset.builder()
                        .id(1L)
                        .name("Crypto")
                        .moneyValue(BigDecimal.valueOf(500.31))
                        .buildAsset(),
                Asset.builder()
                        .id(2L)
                        .name("Account 1")
                        .moneyValue(BigDecimal.valueOf(1500.12))
                        .buildAsset(),
                Asset.builder()
                        .id(3L)
                        .name("Stocks 1")
                        .moneyValue(BigDecimal.valueOf(2201.24))
                        .buildAsset());
                // sum of values : 2000.43 + 2201.24 = 4201.67
                // percentages: 11,91 %, 35,70%, 52,39%
    }

}
