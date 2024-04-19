package personal.finance.iam.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import personal.finance.iam.domain.PasswordResetTokenRepository;
import personal.finance.iam.domain.UserRepository;
import personal.finance.iam.domain.VerificationTokenRepository;
import personal.finance.iam.infrastracture.email.EmailSenderServiceImpl;
import personal.finance.iam.infrastracture.email.EmailSenderServiceMock;
import personal.finance.iam.infrastracture.persistance.repository.InMemoryPasswordResetTokenRepository;
import personal.finance.iam.infrastracture.persistance.repository.InMemoryUserRepository;
import personal.finance.iam.infrastracture.persistance.repository.InMemoryVerificationTokenRepository;
import personal.finance.iam.infrastracture.persistance.repository.UserRepositorySql;
import personal.finance.iam.infrastracture.security.JwtServiceImpl;
import personal.finance.iam.infrastracture.security.JwtServiceMock;

@Configuration
public class AccessManagementConfiguration {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepositorySql userRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    EmailSenderServiceImpl emailSenderService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtServiceImpl jwtService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public AccessManagementFacade accessManagementFacade() {
        return new AccessManagementFacade(userRepository, passwordEncoder, verificationTokenRepository,
            passwordResetTokenRepository, emailSenderService,
            authenticationManager, jwtService, applicationEventPublisher);
    }

    public AccessManagementFacade accessManagementFacadeTest() {
        PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();
        UserRepository userRepository = new InMemoryUserRepository().clear();

        ProviderManager providerManager = configureProviderManagerForTest(passwordEncoder, userRepository);

        return new AccessManagementFacade(
            userRepository, passwordEncoder, new InMemoryVerificationTokenRepository().clear(),
            new InMemoryPasswordResetTokenRepository().clear(),
            new EmailSenderServiceMock(), providerManager, new JwtServiceMock(), new ApplicationEventPublisherMock()
        );
    }

    private static ProviderManager configureProviderManagerForTest(PasswordEncoder passwordEncoder,
        UserRepository userRepository) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        CustomUserDetailsManager customUserDetailsManager =
            new CustomUserDetailsManager(userRepository, passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsManager);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
