package personal.finance.iam.application;

import org.springframework.security.core.Authentication;

public interface AuthTokenService {
    String token(Authentication authentication);
}
