package personal.finance.summary.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserInformation;
import personal.finance.iam.domain.UserRepository;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.infrastracture.persistance.repository.SummaryInMemoryRepository;
import personal.finance.summary.infrastracture.persistance.repository.SummaryRepositorySql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAutoConfiguration
class SummaryConfiguration {

    @Autowired
    private SummaryRepositorySql summarySQLRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepositorySql;

    SummaryFacade summaryFacadeTest() {
        return new SummaryFacade(new SummaryInMemoryRepository().clear());
    }

    @Bean
    SummaryFacade summaryFacade(SummaryRepository summaryRepository) {
        return new SummaryFacade(summaryRepository);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    @Transactional
    public void init() throws Exception {

        User user = new User();
        UserId u1 = UserId.random();
        user.setId(u1);
        user.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(encoder.encode("123"))
            .username("username")
            .email("user@gmail.com")
            .build());

        userRepositorySql.save(user);

        User user2 = new User();
        UserId u2 = UserId.random();
        user2.setId(u2);
        user2.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(encoder.encode("123"))
            .username("user@onet.pl")
            .email("user@onet.pl")
            .build());

        userRepositorySql.save(user2);

        List<Asset> assets = Arrays.asList(
            Asset.builder()
                .id(new AssetId(1L))
                .name("Crypto")
                .money(new Money(BigDecimal.valueOf(500.31)))
                .items(List.of(
                    Item.builder().id(new ItemId(1L)).money(new Money(BigDecimal.valueOf(200.00))).build(),
                    Item.builder().id(new ItemId(2L)).money(new Money(BigDecimal.valueOf(300.31))).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(new AssetId(2L))
                .name("Account 1")
                .money(new Money(BigDecimal.valueOf(1500.12)))
                .items(List.of(
                    Item.builder().id(new ItemId(3L)).money(new Money(BigDecimal.valueOf(200.00))).build(),
                    Item.builder().id(new ItemId(4L)).money(new Money(BigDecimal.valueOf(300.01))).build(),
                    Item.builder().id(new ItemId(5L)).money(new Money(BigDecimal.valueOf(1000.11))).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(new AssetId(3L))
                .name("Stocks 1")
                .money(new Money(BigDecimal.valueOf(2201.24)))
                .items(List.of(
                    Item.builder().id(new ItemId(6L)).money(new Money(BigDecimal.valueOf(201.20))).build(),
                    Item.builder().id(new ItemId(7L)).money(new Money(BigDecimal.valueOf(2000.04))).build()
                ))
                .buildAsset()
        );

        List<Summary> summaryEntities = summarySQLRepository.saveAll(
            Arrays.asList(
                Summary.builder().id(new SummaryId(1L)).userId(u1.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(500.31)))
                    .date(LocalDateTime.of(2022, 1, 1, 0, 0)).build(),
                Summary.builder().id(new SummaryId(2L)).userId(u1.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(1500.12)))
                    .date(LocalDateTime.of(2022, 2, 1, 0, 0)).build(),
                Summary.builder().id(new SummaryId(3L)).userId(u2.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(2201.24)))
                    .date(LocalDateTime.of(2022, 3, 1, 0, 0)).build(),
                Summary.builder().id(new SummaryId(4L)).userId(u2.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(3127.59)))
                    .date(LocalDateTime.of(2022, 4, 1, 0, 0)).build())
        );


        Summary summary = summaryEntities.get(1);
        summary.addAsset(assets.get(0));
        summary.addAsset(assets.get(1));
        summarySQLRepository.save(summary);

        Summary summary1 = summaryEntities.get(2);
        summary1.addAsset(assets.get(2));

        summarySQLRepository.save(summary1);
    }
}
