package personal.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetRepository;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemRepository;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@SpringBootApplication
@EnableJpaRepositories
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
    @Transactional
    public void run(String... args) throws Exception {
        List<Asset> assets = Arrays.asList(
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

        summaryRepository.saveAll(
                Arrays.asList(SummaryEntity.builder().id(1L).moneyValue(BigDecimal.valueOf(500.31)).date(LocalDate.of(2022, 1, 1)).build(),
                        SummaryEntity.builder().id(2L).moneyValue(BigDecimal.valueOf(1500.12)).date(LocalDate.of(2022, 2, 1)).build(),
                        SummaryEntity.builder().id(3L).moneyValue(BigDecimal.valueOf(2201.24)).date(LocalDate.of(2022, 3, 1)).build(),
                        SummaryEntity.builder().id(4L).moneyValue(BigDecimal.valueOf(3127.59)).date(LocalDate.of(2022, 4, 1)).build())
        );

        List<SummaryEntity> all = summaryRepository.findAll();

        for (int i = 0; i < assets.size(); i++) {
            SummaryEntity entity = all.get(i);
            entity.addAsset(assets.get(i));
            summaryRepository.save(entity);
        }

    }


}
