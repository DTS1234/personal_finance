package personal.finance.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.asset.domain.CustomItem;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.domain.StockItem;
import personal.finance.tracking.summary.application.CurrencyManager;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.infrastracture.persistance.repository.SummaryRepositorySql;

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
    @Autowired
    private AssetRepository assetSqlRepository;

    @Value("${run.script}")
    boolean runScript;

    @Bean
    @Transactional
    public Object init(UserSubscriptionQueryRepository userSubscriptionQueryRepository) throws Exception {
        if (!runScript) {
            return null;
        }
        currencyManager.updateExchangeRates();

        User user = userRepositorySql.save(userOne());
        UserId u1 = user.getId();
        // user subscription
        UserSubscription userSubscription = userOneSubscription(user);
        userSubscriptionQueryRepository.save(userSubscription);

        userRepositorySql.save(user);
        // stripe user 1 code customer
        customerRepository.save(
            new Customer(new CustomerId(u1.value), user.getUserInformation().getEmail(), "cus_Phz6xa4OHy10mB", null, null));

        User user2 = userRepositorySql.save(userTwo());
        UserId u2 = user2.getId();
        // stripe user 2 code customer
        customerRepository.save(
            new Customer(new CustomerId(u2.value), user2.getUserInformation().getEmail(), "cus_Phz60VNLojAeut", null, null));

        userRepositorySql.save(userThree());

        AssetId assetIdOne = AssetId.random();
        AssetId assetIdTwo = AssetId.random();
        AssetId assetIdThree = AssetId.random();

        SummaryId firstId = SummaryId.random();
        SummaryId secondId = SummaryId.random();
        SummaryId thirdId = SummaryId.random();
        SummaryId fourthId = SummaryId.random();

        List<Asset> assets = Arrays.asList(
            assetCrypto(assetIdOne, firstId), // summary 1 user 1
            assetAccounts(assetIdTwo, secondId), // summary 2 user 2
            assetStocks(assetIdThree, thirdId)
        );

        assetSqlRepository.saveAll(assets);

        summarySQLRepository.saveAll(
            Arrays.asList(
                summaryOnlyCrypto(firstId, u1),
                summaryAccounts(secondId, u1),
                summaryStocks(thirdId, u2))
        );

        return null;
    }

    private static Summary summaryFourth(SummaryId fourthId, UserId u2) {
        return Summary.builder()
            .id(fourthId).userId(u2.value)
            .state(SummaryState.CONFIRMED)
            .money(new Money(BigDecimal.valueOf(3127.59)))
            .date(LocalDateTime.of(2022, 4, 1, 0, 0))
            .build();
    }

    private static Summary summaryStocks(SummaryId thirdId, UserId u2) {
        return Summary.builder()
            .id(thirdId)
            .userId(u2.value).state(SummaryState.CONFIRMED)
            .money(new Money(BigDecimal.valueOf(2201.24)))
            .date(LocalDateTime.of(2022, 3, 1, 0, 0)).build();
    }

    private static Summary summaryAccounts(SummaryId secondId, UserId u1) {
        return Summary.builder().id(secondId).userId(u1.value).state(SummaryState.CONFIRMED)
            .money(new Money(BigDecimal.valueOf(1500.12)))
            .date(LocalDateTime.of(2022, 2, 1, 0, 0)).build();
    }

    private static Summary summaryOnlyCrypto(SummaryId firstId, UserId u1) {
        return Summary.builder().id(firstId).userId(u1.value).state(SummaryState.CONFIRMED)
            .money(new Money(BigDecimal.valueOf(500.31)))
            .date(LocalDateTime.of(2022, 1, 1, 0, 0)).build();
    }

    private static Asset assetStocks(AssetId assetIdThree, SummaryId fourthId) {
        return Asset.builder()
            .id(assetIdThree)
            .name("Stocks 1")
            .type(AssetType.STOCK)
            .money(new Money(BigDecimal.valueOf(2201.24)))
            .items(List.of(
                StockItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(201.20)))
                    .name("Tesla")
                    .quantity(BigDecimal.valueOf(2))
                    .ticker("TSLA")
                    .currentPrice(new Money(2201.24/2))
                    .purchasePrice(new Money(1000.00))
                    .build(),
                StockItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(2000.04)))
                    .name("Facebook")
                    .quantity(BigDecimal.valueOf(3))
                    .ticker("FCB")
                    .currentPrice(new Money(2000.04/3))
                    .purchasePrice(new Money(500.00))
                    .build()
            ))
            .summaryId(fourthId)
            .buildAsset();
    }

    private static Asset assetAccounts(AssetId assetIdTwo, SummaryId thirdId) {
        return Asset.builder()
            .id(assetIdTwo)
            .name("Account 1")
            .money(new Money(BigDecimal.valueOf(1500.12)))
            .type(AssetType.CUSTOM)
            .items(List.of(
                CustomItem.builder().id(ItemId.random())
                    .name("EUR account")
                    .money(new Money(BigDecimal.valueOf(200.00))).build(),
                CustomItem.builder().id(ItemId.random())
                    .money(new Money(BigDecimal.valueOf(300.01)))
                    .name("USD account")
                    .build(),
                CustomItem.builder().id(ItemId.random())
                    .money(new Money(BigDecimal.valueOf(1000.11)))
                    .name("PLN account")
                    .build()
            ))
            .summaryId(thirdId)
            .buildAsset();
    }

    private static Asset assetCrypto(AssetId assetIdOne, SummaryId secondId) {
        return Asset.builder()
            .id(assetIdOne)
            .name("Crypto")
            .money(new Money(BigDecimal.valueOf(500.31)))
            .type(AssetType.STOCK)
            .items(List.of(
                StockItem.builder().id(ItemId.random())
                    .name("Bitcoin")
                    .quantity(BigDecimal.ONE)
                    .money(new Money(BigDecimal.valueOf(200.00)))
                    .purchasePrice(new Money(100.00))
                    .currentPrice(new Money(200.00))
                    .ticker("BTC")
                    .build(),
                StockItem.builder().id(ItemId.random())
                    .name("Ethereum")
                    .quantity(BigDecimal.ONE)
                    .purchasePrice(new Money(100.00))
                    .currentPrice(new Money(300.31))
                    .ticker("BTC")
                    .money(new Money(BigDecimal.valueOf(300.31))).build()
            ))
            .summaryId(secondId)
            .buildAsset();
    }

    private static UserSubscription userOneSubscription(User user) {
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserSubscriptionId(UserSubscriptionId.random());
        userSubscription.setSubscriptionType(SubscriptionType.PREMIUM);
        userSubscription.setExpires(LocalDate.of(2024, 4, 30));
        userSubscription.setStart(LocalDate.of(2024, 4, 20));
        userSubscription.setUser(user);
        return userSubscription;
    }

    private User userOne() {
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
        return user;
    }

    private User userTwo() {
        User user2 = new User();
        UserId u2 = UserId.random();
        user2.setId(u2);
        user2.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(encoder.encode("123"))
            .username("user@onet.pl")
            .email("user@onet.pl")
            .build());
        return user2;
    }

    private User userThree() {
        User user3 = new User();
        UserId u3 = UserId.random();
        user3.setId(u3);
        user3.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(encoder.encode("123"))
            .username("user@empty.pl")
            .email("user@empty.pl")
            .build());
        return user3;
    }
}
