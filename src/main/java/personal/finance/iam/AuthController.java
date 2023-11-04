package personal.finance.iam;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsManager userDetailsManager;
    private final JwtGenerator jwtGenerator;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return authenticate(loginRequest.username, loginRequest.password);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignupRequest signupRequest) {
        userDetailsManager.createUser(
            User.builder()
                .username(signupRequest.username())
                .password(signupRequest.password())
                .roles("USER")
                .build()
        );
        return authenticate(signupRequest.username(), signupRequest.password);
    }

    private ResponseEntity<AuthResponse> authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(new AuthResponse(username, jwtGenerator.token(authentication), "36000"));
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    public record LoginRequest(String username, String password) {

    }

    public record SignupRequest(String username, String password) {

    }

    public record AuthResponse(String username, String token, String expiresIn) {

    }
}
