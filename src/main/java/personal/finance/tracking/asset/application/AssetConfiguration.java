package personal.finance.tracking.asset.application;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import personal.finance.common.events.EventPublisher;
import personal.finance.common.events.FakeEventPublisher;
import personal.finance.common.events.SpringEventPublisher;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetInMemoryRepository;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetJpaRepository;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetModelMapper;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetSqlRepository;

@Configuration
@EnableAutoConfiguration
public class AssetConfiguration {

    @Autowired
    AssetJpaRepository assetJpaRepository;
    @Autowired
    AssetModelMapper assetModelMapper;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public AssetFacade assetFacadeTest() {
        return new AssetFacade(new AssetInMemoryRepository().clear(), new FakeEventPublisher());
    }

    @Bean
    public AssetFacade assetFacade() {
        return new AssetFacade(assetRepository(), eventPublisher());
    }

    @Bean
    public AssetRepository assetRepository() {
        return new AssetSqlRepository(assetModelMapper, assetJpaRepository);
    }

    @Bean
    public EventPublisher eventPublisher() {
       return new SpringEventPublisher(applicationEventPublisher);
    }
}
