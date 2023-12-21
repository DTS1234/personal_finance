package personal.finance.summary.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.infrastracture.persistance.repository.SummaryInMemoryRepository;

@Configuration
@EnableAutoConfiguration
class SummaryConfiguration {

    @Autowired
    private personal.finance.summary.application.UserRepository userRepository;

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
}
