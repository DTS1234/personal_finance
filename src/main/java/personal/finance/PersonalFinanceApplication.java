package personal.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@SpringBootApplication
public class PersonalFinanceApplication implements CommandLineRunner {

    @Autowired
    AssetRepository assetRepository;
    @Autowired
    SummaryRepository summaryRepository;
    @Autowired
    ItemRepository itemRepository;

    public static void main(String[] args) {
        SpringApplication.run(PersonalFinanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        assetRepository.saveAll(Arrays.asList(
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
                        .buildAsset()));

        Asset asset1 = assetRepository.findById(1L).get();
        itemRepository.saveAll(Arrays.asList(
                Item.builder().id(1L).moneyValue(BigDecimal.valueOf(500.21)).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").asset(asset1).build(),
                Item.builder().id(2L).moneyValue(BigDecimal.valueOf(1000.22)).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").asset(asset1).build(),
                Item.builder().id(3L).moneyValue(BigDecimal.valueOf(500.01)).quantity(BigDecimal.valueOf(100.00)).name("Luna").asset(asset1).build()));

        Asset asset2 = assetRepository.findById(2L).get();
        itemRepository.save(Item.builder().moneyValue(BigDecimal.valueOf(2201.24)).id(4L).quantity(BigDecimal.valueOf(1)).name("ZL account").asset(asset2).build());


        Asset asset3 = assetRepository.findById(3L).get();
        itemRepository.saveAll(Arrays.asList(
                Item.builder().id(5L).moneyValue(BigDecimal.valueOf(1201.12)).quantity(BigDecimal.valueOf(12)).name("CDP").asset(asset3).build(),
                Item.builder().id(6L).moneyValue(BigDecimal.valueOf(1000.12)).quantity(BigDecimal.valueOf(43)).name("ALG").asset(asset3).build()));

        summaryRepository.saveAll(
                Arrays.asList(Summary.builder().id(1L).moneyValue(500.31).date(LocalDate.of(2022, 1, 1)).build(),
                        Summary.builder().id(2L).moneyValue(1500.12).date(LocalDate.of(2022, 2, 1)).build(),
                        Summary.builder().id(3L).moneyValue(2201.24).date(LocalDate.of(2022, 3, 1)).build(),
                        Summary.builder().id(4L).moneyValue(3127.59).date(LocalDate.of(2022, 4, 1)).build())
        );

    }


}
