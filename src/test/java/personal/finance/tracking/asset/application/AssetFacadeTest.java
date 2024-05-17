package personal.finance.tracking.asset.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.common.events.Event;
import personal.finance.common.events.EventPublisher;
import personal.finance.common.events.FakeEventPublisher;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.domain.events.AssetCreated;
import personal.finance.tracking.asset.domain.events.AssetUpdated;
import personal.finance.tracking.summary.application.dto.AssetDTO;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.SummaryId;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static personal.finance.tracking.asset.domain.AssetType.NORMAL;

@DisplayNameGeneration(ReplaceUnderscores.class)
class AssetFacadeTest {

    private final AssetFacade assetFacade = new AssetConfiguration().assetFacadeTest();

    @Test
    void should_update_an_asset() {
        // given
        UUID summaryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        // and one asset created
        Asset oldAsset = Asset.builder()
            .money(new Money(0)).type(NORMAL)
            .items(Collections.emptyList())
            .name("Asset old")
            .summaryId(new SummaryId(summaryId))
            .buildAsset();
        assetFacade.createAsset(summaryId, oldAsset);

        // when updating an asset
        Item item = Item.builder().id(ItemId.random()).quantity(BigDecimal.ONE).name("New Item").money(new Money(10)).build();
        Asset updatedAsset = new Asset(new AssetId(oldAsset.getIdValue()), new Money(10), "Asset updated", List.of(item), new SummaryId(summaryId));
        Asset asset = assetFacade.updateAsset(userId, summaryId, oldAsset.getIdValue(), updatedAsset);

        // then expect the update to occur
        assertThat(asset.getName()).isEqualTo("Asset updated");
        assertThat(asset.getMoney()).isEqualTo(new Money(10));
        assertThat(asset.getSummaryId()).isEqualTo(new SummaryId(summaryId));
        assertThat(asset.getItems()).isEqualTo(List.of(item));

        FakeEventPublisher eventPublisher = (FakeEventPublisher) assetFacade.getEventPublisher();
        AssetUpdated assetUpdated = (AssetUpdated) eventPublisher.publishedStore.get(1);

        assertThat(assetUpdated.eventId).isNotNull();
        assertThat(assetUpdated.timestamp).isNotNull();
    }

    @Test
    void should_add_an_asset() {
        UUID summaryId = UUID.randomUUID();
        Asset newAsset = Asset.builder()
            .money(new Money(0)).type(NORMAL)
            .items(Collections.emptyList())
            .name("Asset 1")
            .summaryId(new SummaryId(summaryId))
            .buildAsset();

        Asset asset = assetFacade.createAsset(summaryId, newAsset);

        assertThat(asset).isNotNull();
        assertThat(asset.getId()).isNotNull();
        assertThat(asset.getType()).isEqualTo(NORMAL);
        assertThat(asset.getMoney()).isEqualTo(new Money(0));
        assertThat(asset.getName()).isEqualTo("Asset 1");
        assertThat(asset.getItems()).isEmpty();
        assertThat(asset.getSummaryId()).isEqualTo(new SummaryId(summaryId));

        FakeEventPublisher eventPublisher = (FakeEventPublisher)assetFacade.getEventPublisher();
        AssetCreated assetCreated = (AssetCreated) eventPublisher.publishedStore.getFirst();

        assertThat(assetCreated.eventId).isNotNull();
        assertThat(assetCreated.summaryId).isEqualTo(summaryId);
        assertThat(assetCreated.timestamp).isNotNull();
        assertThat(assetCreated.asset).isEqualTo(asset);
    }
}