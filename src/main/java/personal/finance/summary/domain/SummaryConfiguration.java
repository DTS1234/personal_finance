package personal.finance.summary.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SummaryConfiguration {

    SummaryFacade summaryFacade() {
        return summaryFacade(new SummaryInMemoryRepository());
    }

    @Bean
    SummaryFacade summaryFacade(SummaryRepository summaryRepository) {
        return new SummaryFacade(summaryRepository);
    }

}
