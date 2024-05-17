package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import personal.finance.PersonalFinanceApplication;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.iam.application.AuthTokenService;
import personal.finance.iam.application.CustomUserDetailsManager;
import personal.finance.iam.application.EmailSenderService;
import personal.finance.iam.domain.PasswordResetTokenRepository;
import personal.finance.iam.domain.UserRepository;
import personal.finance.iam.domain.VerificationToken;
import personal.finance.iam.domain.VerificationTokenRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@SpringBootTest(classes = {
    PersonalFinanceApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ObjectMapper objectMapper;

    private ApplicationEventPublisher publisher;

    AuthenticationManager configAuthManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(new BCryptPasswordEncoder(12));
        CustomUserDetailsManager customUserDetailsManager = new CustomUserDetailsManager(userRepository, passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsManager);
        return new ProviderManager(daoAuthenticationProvider);
    }

    String getFile(String fileName) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
        return Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.joining(System.lineSeparator())).trim();
    }

    String getVerificationToken(String userName) {
        VerificationToken byUser = verificationTokenRepository.findByUser(userName);
        return byUser.getToken();
    }
}
