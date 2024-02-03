package personal.finance.summary.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.UserRepository;
import personal.finance.summary.infrastracture.persistance.repository.SummaryInMemoryRepository;
import personal.finance.summary.infrastracture.persistance.repository.UserInMemoryRepository;

@Configuration
@EnableAutoConfiguration
class SummaryConfiguration {

    @Autowired
    private UserRepository userRepository;

    SummaryFacade summaryFacadeTest() {
        UserInMemoryRepository userRepository1 = new UserInMemoryRepository();
        return new SummaryFacade(new SummaryInMemoryRepository().clear(), userRepository1);
    }

    @Bean
    SummaryFacade summaryFacade(SummaryRepository summaryRepository) {
        return new SummaryFacade(summaryRepository, userRepository);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
