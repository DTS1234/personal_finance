package personal.finance.tracking.summary.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.UserRepository;
import personal.finance.tracking.summary.infrastracture.persistance.repository.SummaryInMemoryRepository;
import personal.finance.tracking.summary.infrastracture.persistance.repository.UserInMemoryRepository;

@Configuration
@EnableAutoConfiguration
public class SummaryConfiguration {

    @Autowired
    private UserRepository userRepository;

    public SummaryFacade summaryFacadeTest() {
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
