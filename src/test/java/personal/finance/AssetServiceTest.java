package personal.finance;

import acceptance.GivenAssets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import personal.finance.asset.AssetRepository;
import personal.finance.asset.AssetService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
class AssetServiceTest {

    private AssetService subject;
    @Mock
    private AssetRepository assetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new AssetService(assetRepository);
    }

    @Test
    void shouldReturnIdPercentageMapFromAllAssetsSaved() {
        //given
        when(assetRepository.findAll()).thenReturn(GivenAssets.assets());
        when(assetRepository.selectAssetMoneyValueSum()).thenReturn(4201.67);
        //when
        Map<Long, Double> actual = subject.getPercentages();
        //then
        Map<Long, Double> expected = new HashMap<>();
        expected.put(1L, 11.91);
        expected.put(2L, 35.70);
        expected.put(3L, 52.39);

        Assertions.assertThat(actual)
                .hasSize(3)
                .containsExactlyEntriesOf(expected);
    }
}
