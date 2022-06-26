package personal.finance.summary.domain.action;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;

import java.util.ArrayList;
import java.util.Collections;

class AddAssetDraftActionTest {

    @Mock
    PersistenceAdapter persistenceAdapter;

    private AddAssetDraftAction subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new AddAssetDraftAction(persistenceAdapter);
    }

    @Test
    void givenSummaryNotInDraft_shouldThrowAnException() {
        Assertions.assertThatThrownBy(() -> subject.execute(AssetDomain.builder().build(), SummaryDomain.builder().state(SummaryState.CONFIRMED).build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("You cannot add that asset in %s state.", SummaryState.CONFIRMED));
    }

    @Test
    void givenSummaryAlreadyContainsAssetWithThatName_shouldThrowAnException() {

        AssetDomain assetDomain = AssetDomain.builder().name("name1").build();
        SummaryDomain summary = SummaryDomain.builder().state(SummaryState.DRAFT).assets(Collections.singletonList(assetDomain)).build();

        Assertions.assertThatThrownBy(() -> subject.execute(assetDomain, summary))
                .isInstanceOf(AssetWithThatNameAlreadyExistsException.class);
    }

    @Test
    void givenNewValidAssetPassed_shouldAddItToSummaryAndPersist() {
        AssetDomain asset1 = AssetDomain.builder().name("name1").build();
        AssetDomain asset2 = AssetDomain.builder().name("name2").build();
        SummaryDomain summary = SummaryDomain.builder().state(SummaryState.DRAFT).assets(new ArrayList<>(Collections.singleton(asset1))).build();

        subject.execute(asset2, summary);

        Assertions.assertThat(summary.getAssets()).containsExactlyInAnyOrder(asset1, asset2);
        Mockito.verify(persistenceAdapter, Mockito.times(1)).addAssetToSummary(asset2,summary);
    }
}
