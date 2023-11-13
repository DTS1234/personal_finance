package personal.finance.iam.infrastracture.security;

import org.springframework.security.core.Authentication;
import personal.finance.iam.application.AuthTokenService;

public class JwtServiceMock implements AuthTokenService {

    @Override
    public String token(Authentication authentication) {
        return "jwt mock token";
    }
}
