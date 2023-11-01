package personal.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.summary.domain.model.SummaryState;
import personal.finance.summary.domain.model.Asset;
import personal.finance.summary.domain.model.Item;
import personal.finance.summary.domain.model.Summary;
import personal.finance.summary.persistance.SummarySQLRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@SpringBootApplication
@EnableJpaRepositories
public class PersonalFinanceApplication implements CommandLineRunner {
    @Autowired
    SummarySQLRepository summarySQLRepository;

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
                .items(List.of(
                    Item.builder().moneyValue(BigDecimal.valueOf(200.00)).build(),
                    Item.builder().moneyValue(BigDecimal.valueOf(300.31)).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(2L)
                .name("Account 1")
                .moneyValue(BigDecimal.valueOf(1500.12))
                .items(List.of(
                    Item.builder().moneyValue(BigDecimal.valueOf(200.00)).build(),
                    Item.builder().moneyValue(BigDecimal.valueOf(300.01)).build(),
                    Item.builder().moneyValue(BigDecimal.valueOf(1000.11)).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(3L)
                .name("Stocks 1")
                .moneyValue(BigDecimal.valueOf(2201.24))
                .items(List.of(
                    Item.builder().moneyValue(BigDecimal.valueOf(201.20)).build(),
                    Item.builder().moneyValue(BigDecimal.valueOf(2000.04)).build()
                ))
                .buildAsset()
        );

        List<Summary> summaryEntities = summarySQLRepository.saveAll(
            Arrays.asList(
                Summary.builder().id(1L).state(SummaryState.CONFIRMED).moneyValue(BigDecimal.valueOf(500.31)).date(LocalDateTime.of(2022, 1, 1, 0, 0)).build(),
                Summary.builder().id(2L).state(SummaryState.CONFIRMED).moneyValue(BigDecimal.valueOf(1500.12)).date(LocalDateTime.of(2022, 2, 1, 0, 0)).build(),
                Summary.builder().id(3L).state(SummaryState.CONFIRMED).moneyValue(BigDecimal.valueOf(2201.24)).date(LocalDateTime.of(2022, 3, 1, 0, 0)).build(),
                Summary.builder().id(4L).state(SummaryState.CONFIRMED).moneyValue(BigDecimal.valueOf(3127.59)).date(LocalDateTime.of(2022, 4, 1, 0, 0)).build())
        );

        Summary summary = summaryEntities.get(3);
        summary.addAsset(assets.get(0));
        summary.addAsset(assets.get(1));
        summary.addAsset(assets.get(2));

        summarySQLRepository.save(summary);
    }


}
