package personal.finance.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.application.dto.AuthResponseDTO;
import personal.finance.iam.application.dto.PasswordResetConfirmationDTO;
import personal.finance.iam.application.dto.PasswordResetState;
import personal.finance.iam.application.dto.RegistrationState;
import personal.finance.iam.application.dto.UserRegistrationConfirmationDTO;
import personal.finance.iam.application.dto.UserRegistrationDTO;
import personal.finance.iam.domain.PasswordResetToken;
import personal.finance.iam.domain.PasswordResetTokenRepository;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserRepository;
import personal.finance.iam.domain.VerificationToken;
import personal.finance.iam.domain.VerificationTokenRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
public class AccessManagementFacade {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    public UserRegistrationConfirmationDTO registerUser(UserRegistrationDTO registrationDto) {
        User newUser = new User();
        newUser.setEmail(registrationDto.email());
        newUser.setUsername(registrationDto.email());
        newUser.setPassword(passwordEncoder.encode(registrationDto.password()));
        newUser.setEnabled(false); // User is not enabled until they verify their email

        if (userRepository.findByEmail(registrationDto.email()) != null) {
            throw new IllegalStateException("User with this email address already exists");
        }

        User userSaved = userRepository.save(newUser);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, userSaved);
        tokenRepository.save(verificationToken);

        emailSenderService.sendEmailConfirmation(newUser.getEmail(), token);

        return new UserRegistrationConfirmationDTO("User email address "
            + newUser.getEmail() + " confirmation in progress.",
            RegistrationState.IN_PROGRESS);
    }

    public UserRegistrationConfirmationDTO confirmRegistration(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken != null && !verificationToken.isExpired()) {
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            return new UserRegistrationConfirmationDTO("User confirmed", RegistrationState.SUCCESS);
        } else {
            return new UserRegistrationConfirmationDTO("User confirmation failed", RegistrationState.FAILED);
        }
    }

    public AuthResponseDTO login(String username, String password) {
        try {
            Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User found = userRepository.findByEmail(username);
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return new AuthResponseDTO(username, found.getId(),
                    authTokenService.token(authentication), "3600");
            } else {
                return null;
            }

        } catch (LockedException lockedException) {
            throw new IllegalStateException("User email address is not confirmed.");
        } catch (BadCredentialsException badCredentialsException) {
            throw new IllegalStateException("Given credentials are incorrect.");
        } catch (AuthenticationException authenticationException) {
            throw new IllegalStateException("Error occurred while authenticating the user.");
        }
    }


    public final PasswordResetConfirmationDTO requestPasswordReset(String email) {
        User userFound = userRepository.findByEmail(email);

        if (userFound == null) {
            throw new IllegalStateException("User with this email address does not exist.");
        }

        if (!userFound.isEnabled()) {
            throw new IllegalStateException("User email address is not confirmed.");
        }

        PasswordResetToken passwordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), userFound);
        PasswordResetToken token = passwordResetTokenRepository.save(passwordResetToken);
        emailSenderService.sendPasswordReset(userFound.getEmail(), token.getToken());

        return new PasswordResetConfirmationDTO("Email with password reset link was sent",
            PasswordResetState.IN_PROGRESS);
    }

    public PasswordResetConfirmationDTO resetPassword(String token, String newPassword) {

        PasswordResetToken tokenFound = passwordResetTokenRepository.findByToken(token);

        if (tokenFound == null || !tokenFound.getToken().equals(token)) {
            throw new IllegalStateException("Reset password link is invalid.");
        }

        if (tokenFound.isExpired()) {
            throw new IllegalStateException("Reset password link is expired");
        }

        User user = tokenFound.getUser();

        if (!user.isEnabled()) {
            throw new IllegalStateException("User email address is not confirmed.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new PasswordResetConfirmationDTO("Password changed successfully", PasswordResetState.SUCCESS);
    }
}
