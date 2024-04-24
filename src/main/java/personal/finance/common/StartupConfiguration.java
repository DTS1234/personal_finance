package personal.finance.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.domain.SubscriptionType;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserInformation;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;
import personal.finance.iam.query.UserSubscriptionQueryRepository;
import personal.finance.payment.domain.Customer;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.domain.CustomerRepository;
import personal.finance.summary.application.CurrencyManager;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.infrastracture.persistance.repository.SummaryRepositorySql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
class StartupConfiguration {

    @Autowired
    private SummaryRepositorySql summarySQLRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private personal.finance.iam.domain.UserRepository userRepositorySql;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private CustomerRepository customerRepository;


    @Bean
    @Transactional
    public Object init(UserSubscriptionQueryRepository userSubscriptionQueryRepository) throws Exception {
        currencyManager.updateExchangeRates();

        User user = new User();
        UserId u1 = UserId.random();
        user.setId(u1);
        user.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(encoder.encode("123"))
            .username("username")
            .email("user@gmail.com")
            .firstname("Stefanos")
            .lastname("Kozakos")
            .gender("Male")
            .birthdate(LocalDate.of(1999, 7, 11))
            .build());

        userRepositorySql.save(user);

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserSubscriptionId(UserSubscriptionId.random());
        userSubscription.setSubscriptionType(SubscriptionType.PREMIUM);
        userSubscription.setExpires(LocalDate.of(2024,4, 30));
        userSubscription.setStart(LocalDate.of(2024, 4, 20));
        userSubscription.setUser(user);
        userSubscriptionQueryRepository.save(userSubscription);

        user.setUserSubscription(userSubscription);
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

        customerRepository.save(
            new Customer(new CustomerId(user.getId().value), user.getUserInformation().getEmail(), "cus_Phz6xa4OHy10mB",
                null));
        customerRepository.save(new Customer(new CustomerId(user2.getId().value), user2.getUserInformation().getEmail(),
            "cus_Phz60VNLojAeut", null));

        AssetId assetIdOne = AssetId.random();
        AssetId assetIdTwo = AssetId.random();
        AssetId assetIdThree = AssetId.random();

        List<Asset> assets = Arrays.asList(
            Asset.builder()
                .id(assetIdOne)
                .name("Crypto")
                .money(new Money(BigDecimal.valueOf(500.31)))
                .items(List.of(
                    Item.builder().id(ItemId.random())
                        .name("Bitcoin")
                        .quantity(BigDecimal.ONE)
                        .money(new Money(BigDecimal.valueOf(200.00))).build(),
                    Item.builder().id(ItemId.random())
                        .name("Ethereum")
                        .quantity(BigDecimal.TEN)
                        .money(new Money(BigDecimal.valueOf(300.31))).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(assetIdTwo)
                .name("Account 1")
                .money(new Money(BigDecimal.valueOf(1500.12)))
                .items(List.of(
                    Item.builder().id(ItemId.random())
                        .name("EUR account")
                        .quantity(BigDecimal.ONE)
                        .money(new Money(BigDecimal.valueOf(200.00))).build(),
                    Item.builder().id(ItemId.random())
                        .money(new Money(BigDecimal.valueOf(300.01)))
                        .name("USD account")
                        .quantity(BigDecimal.ONE)
                        .build(),
                    Item.builder().id(ItemId.random())
                        .money(new Money(BigDecimal.valueOf(1000.11)))
                        .name("PLN account")
                        .quantity(BigDecimal.ONE)
                        .build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(assetIdThree)
                .name("Stocks 1")
                .money(new Money(BigDecimal.valueOf(2201.24)))
                .items(List.of(
                    Item.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(201.20)))
                        .name("Tesla")
                        .quantity(BigDecimal.valueOf(2))
                        .build(),
                    Item.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(2000.04)))
                        .name("Facebook")
                        .quantity(BigDecimal.valueOf(3))
                        .build()
                ))
                .buildAsset()
        );

        SummaryId firstId = SummaryId.random();
        SummaryId secondId = SummaryId.random();
        SummaryId thirdId = SummaryId.random();
        SummaryId fourthId = SummaryId.random();

        List<Summary> summaryEntities = summarySQLRepository.saveAll(
            Arrays.asList(
                Summary.builder().id(firstId).userId(u1.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(500.31)))
                    .date(LocalDateTime.of(2022, 1, 1, 0, 0)).build(),
                Summary.builder().id(secondId).userId(u1.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(1500.12)))
                    .date(LocalDateTime.of(2022, 2, 1, 0, 0)).build(),
                Summary.builder().id(thirdId).userId(u2.value).state(SummaryState.CONFIRMED)
                    .money(new Money(BigDecimal.valueOf(2201.24)))
                    .date(LocalDateTime.of(2022, 3, 1, 0, 0)).build(),
                Summary.builder().id(fourthId).userId(u2.value).state(SummaryState.CONFIRMED)
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
        return null;
    }

}
