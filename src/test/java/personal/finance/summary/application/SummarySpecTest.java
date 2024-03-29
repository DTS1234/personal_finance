package personal.finance.summary.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personal.finance.GivenAssets;
import personal.finance.summary.application.dto.DTOMapper;
import personal.finance.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.infrastracture.external.CurrencyFakeProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SummarySpecTest {

    private final SummaryFacade facade = new SummaryConfiguration().summaryFacadeTest();

    @BeforeAll
    static void setUp() {
        CurrencyManager.currencies = new CurrencyFakeProvider().getRates();
    }

    @Test
    void shouldMoveSummaryFromDraftToConfirmed() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when
        Summary confirmSummary = facade.confirmSummary(newSummary.getId().getValue(), userId);

        // then
        Assertions.assertThat(confirmSummary.getState())
            .isEqualTo(SummaryState.CONFIRMED);
    }

    @Test
    void shouldThrowIfNoSummaryFoundForGivenId() {
        assertThatThrownBy(() -> facade.confirmSummary(-1L, UUID.randomUUID()))
            .hasMessage("Summary with id of -1 does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_is_not_in_draft() {
        // given
        UUID userId = UUID.randomUUID();
        facade.createNewSummary(userId);
        facade.cancelSummary(1L, userId);

        // then - exception is thrown
        assertThatThrownBy(() -> facade.confirmSummary(1L, userId))
            .hasMessage("Summary can be confirmed only if it is in DRAFT state.");
    }

    @Test
    void shouldThrowIfUserTriesToConfirmOtherSummaryThanHis() {
        // given
        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();
        Summary summaryFirstUser = facade.createNewSummary(u1);
        facade.createNewSummary(u2);

        assertThatThrownBy(() -> facade.confirmSummary(summaryFirstUser.getId().getValue(), u2))
            .hasMessage("Summary with id of 1 does not exist for this user.");
    }

    @Test
    void shouldCreateNewSummaryWithAllTheAssetsFromTheLastConfirmedSummary() {
        BigDecimal ten = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);

        // given - one cancelled summary
        UUID userId = UUID.randomUUID();
        Summary s1 = facade.createNewSummary(userId);
        s1.cancel();
        // second updated and confirmed summary
        Summary s2 = facade.createNewSummary(userId);
        s2.updateMoneyValue(new Money(ten));
        s2.addAsset(Asset.builder().id(new AssetId(1L))
            .name("Stock GPW")
            .money(new Money(ten))
            .items(List.of(Item.builder().money(new Money(ten))
                .quantity(BigDecimal.ONE)
                .name("Allegro")
                .id(new ItemId(1L))
                .build()))
            .buildAsset());

        facade.updateSummaryInDraft(DTOMapper.dto(s2), userId);
        facade.confirmSummary(s2.getId().getValue(), userId);

        // when
        Summary result = facade.createNewSummary(userId);

        // then
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("assets.id", "assets.items.id")
            .isEqualTo(
                Summary.builder()
                    .id(result.getId())
                    .userId(userId)
                    .date(
                        result.getDate()
                    )
                    .assets(
                        List.of(Asset.builder()
                            .id(new AssetId(2L))
                            .name("Stock GPW")
                            .money(new Money(ten))
                            .items(List.of(
                                Item.builder().money(new Money(ten))
                                    .quantity(BigDecimal.ONE)
                                    .name("Allegro")
                                    .id(new ItemId(1L))
                                    .build()
                            ))
                            .buildAsset())
                    )
                    .state(SummaryState.DRAFT)
                    .money(new Money(ten))
                    .build()
            );
    }

    @Test
    void shouldCreateDefaultEmptySummaryIfThereAreNoConfirmedSummaries() {
        UUID userId = UUID.randomUUID();
        Summary summary = facade.createNewSummary(userId);

        Assertions.assertThat(summary)
            .usingRecursiveComparison()
            .ignoringFields("date")
            .isEqualTo(Summary.builder()
                .id(new SummaryId(1L))
                .userId(userId)
                .money(new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
                .assets(List.of())
                .state(SummaryState.DRAFT)
                .build());
    }

    @Test
    void should_update_summary_in_draft_with_new_asset_and_money_value() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when - new asset is added
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(newSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // and - summary is saved in draft
        Summary updated = facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), userId);

        // then - updated summary is saved
        Assertions.assertThat(updated)
            .usingRecursiveComparison()
            .ignoringFields("assets.items.id", "date")
            .isEqualTo(updatedSummaryInDraft);

        Assertions.assertThat(updated.getDate().truncatedTo(ChronoUnit.MINUTES))
            .isEqualTo(newSummary.getDate().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void should_throw_when_money_value_does_not_match_the_money_sum_of_all_items() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when - new asset is added but money value is wrong
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(newSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4200.67))) // should be 4211.67
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), userId))
            .hasMessage(
                "Invalid money value for the updated summary, it should be equal to the sum of all items money value.");
    }

    @Test
    void should_throw_when_trying_to_update_a_summary_of_other_user() {
        // given
        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();
        Summary firstUserSummary = facade.createNewSummary(u1);
        facade.createNewSummary(u2);

        // when
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(firstUserSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(firstUserSummary.getDate())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67))) // should be 4211.67
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), u2))
            .hasMessage("Summary with id " + updatedSummaryInDraft.getId() + " does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_is_not_in_a_draft_mode() {
        // given
        UUID userId = UUID.randomUUID();
        facade.createNewSummary(userId);
        Summary cancelledSummary = facade.cancelSummary(1L, userId);
        facade.createNewSummary(userId);
        Summary confirmedSummary = facade.confirmSummary(2L, userId);

        // when
        cancelledSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());
        confirmedSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(cancelledSummary), userId))
            .hasMessage("Summary must be in a draft mode to be updated.");
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(confirmedSummary), userId))
            .hasMessage("Summary must be in a draft mode to be updated.");
    }

    @Test
    void should_throw_when_summary_with_given_id_does_not_exist() {
        // when - id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(new SummaryId(-1L))
            .assets(new ArrayList<>(updatedAssets))
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), UUID.randomUUID()))
            .hasMessage("Summary with id -1 does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_creation_date_is_different() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when - new asset is added but id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(new SummaryId(1L))
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate().minusDays(1))
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), userId))
            .hasMessage("Creation date cannot be changed for a summary.");
    }

    @Test
    @DisplayName("should return current summary that is in draft for a given user")
    void shouldReturnSummaryInDraft() {
        //given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when
        Summary currentDraft = facade.getCurrentDraft(userId);

        // then
        assertThat(newSummary).isEqualTo(currentDraft);
    }

    @Test
    @DisplayName("should throw a No summary in draft exception if no summaries in draft for a user")
    void shouldThrowIfNoSummaryInDraft() {
        //given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);
        facade.cancelSummary(newSummary.getId().getValue(), userId);

        // when
        assertThatThrownBy(() -> facade.getCurrentDraft(userId))
            .hasMessage("No summary is being created for this user.")
            .isInstanceOf(NoSummaryInDraftException.class);
    }

    @Test
    @DisplayName("should throw if there user tries to create another summary without confirming or closing the previous one")
    void shouldThrowIfTwoSummariesAreInDraft() {
        //given
        UUID userId = UUID.randomUUID();
        facade.createNewSummary(userId);

        // when
        assertThatThrownBy(() -> facade.createNewSummary(userId))
            .hasMessage("User can have only one summary in creation.");
    }

    @Test
    void shouldCancelSummary() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);
        // when
        facade.cancelSummary(newSummary.getId().getValue(), userId);
        // then
        assertThatThrownBy(() -> facade.getCurrentDraft(userId));
    }

    @Test
    void shouldThrowWhenTryingToCancelOtherUserSummary() {
        // given
        UUID userId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();
        Summary firstSummary = facade.createNewSummary(userId);
        Summary secondSummary = facade.createNewSummary(secondUserId);
        // expect
        assertThatThrownBy(() -> facade.cancelSummary(firstSummary.getId().getValue(), secondUserId))
            .hasMessage("This user does not have a summary with id of " + firstSummary.getId());
    }

    @Test
    void should_return_data_with_correct_currency() {
        // given new summary
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // and currency rate available
        CurrencyManager.currencies = new CurrencyFakeProvider().getRates();

        // and new asset added
        Item build = Item.builder().quantity(BigDecimal.ONE).money(new Money(10)).build();
        newSummary.addAsset(Asset.builder().money(new Money(10)).items(List.of(build)).buildAsset());
        facade.updateSummaryInDraft(DTOMapper.dto(newSummary), userId);

        Money money = newSummary.getMoney();
        System.out.println("before: " + money);

        // when currency updated
        facade.updateCurrency(userId, Currency.PLN);

        // then next fetched summary
        Summary currentDraft = facade.getCurrentDraft(userId);
        Money expected = money.multiplyBy(4.5, Currency.PLN);

        // should have correct currency value and money
        assertThat(currentDraft.getMoney())
            .isEqualTo(expected);
    }


    @Test
    void should_return_data_with_correct_currency_PLN_USD() {
        // given new summary
        CurrencyManager.currencies = new CurrencyFakeProvider().getRates();
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // and new asset added in PLN
        facade.updateCurrency(userId, Currency.PLN);
        Item build = Item.builder().quantity(BigDecimal.ONE).money(new Money(10, Currency.PLN)).build();
        newSummary.addAsset(Asset.builder().money(new Money(10, Currency.PLN)).items(List.of(build)).buildAsset());
        newSummary.setMoney(new Money(10.00, Currency.PLN));
        facade.updateSummaryInDraft(DTOMapper.dto(newSummary), userId);

        // when currency updated
        facade.updateCurrency(userId, Currency.USD);

        // then next fetched summary
        Summary currentDraft = facade.getCurrentDraft(userId);
        Money expected = new Money(2.53, Currency.USD);

        // should have correct currency value and money
        assertThat(currentDraft.getMoney())
            .isEqualTo(expected);
    }

    @Test
    void should_return_data_with_correct_currency_USD_EUR() {
        // given new summary
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // and new asset added in EUR
        Item item1 = Item.builder().quantity(BigDecimal.ONE).money(new Money(10, Currency.EUR)).build();
        Item item2 = Item.builder().quantity(BigDecimal.ONE).money(new Money(15, Currency.EUR)).build();
        newSummary.addAsset(
            Asset.builder().money(new Money(25, Currency.EUR)).items(List.of(item1, item2)).buildAsset());
        newSummary.setMoney(new Money(25.00, Currency.EUR));
        facade.updateSummaryInDraft(DTOMapper.dto(newSummary), userId);

        // when currency updated
        facade.updateCurrency(userId, Currency.USD);

        // then next fetched summary
        Summary currentDraft = facade.getCurrentDraft(userId);
        Money expected = new Money(27.50, Currency.USD);

        // should have correct currency value and money
        assertThat(currentDraft.getMoney())
            .isEqualTo(expected);

        facade.updateCurrency(userId, Currency.PLN);
        currentDraft = facade.getCurrentDraft(userId);
        expected = new Money(108.63, Currency.PLN);

        // should have correct currency value and money
        assertThat(currentDraft.getMoney())
            .isEqualTo(expected);
    }

}
