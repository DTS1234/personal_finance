package personal.finance.iam.infrastracture.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.iam.application.dto.AuthResponseDTO;
import personal.finance.iam.application.dto.LoginRequestDTO;
import personal.finance.iam.application.dto.PasswordRequestDTO;
import personal.finance.iam.application.dto.PasswordResetConfirmationDTO;
import personal.finance.iam.application.dto.ResetPasswordDTO;
import personal.finance.iam.application.dto.UserRegistrationConfirmationDTO;
import personal.finance.iam.application.dto.UserRegistrationDTO;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AccessManagementFacade accessManagementFacade;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticate(loginRequestDTO.username(), loginRequestDTO.password());
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegistrationConfirmationDTO> register(@RequestBody UserRegistrationDTO signupRequest) {
        UserRegistrationConfirmationDTO confirmationDTO = accessManagementFacade.registerUser(signupRequest);
        return ResponseEntity.ok().body(confirmationDTO);
    }

    @GetMapping("/registration/confirm")
    public UserRegistrationConfirmationDTO confirmRegistration(@RequestParam("token") String token) {
        return accessManagementFacade.confirmRegistration(token);
    }

    @PostMapping("/password_reset/request")
    public PasswordResetConfirmationDTO requestPasswordChange(@RequestBody PasswordRequestDTO request) {
        return accessManagementFacade.requestPasswordReset(request.email());
    }

    @PostMapping("/password_reset")
    public PasswordResetConfirmationDTO resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return accessManagementFacade.resetPassword(resetPasswordDTO.token(), resetPasswordDTO.newPassword());
    }

    private ResponseEntity<AuthResponseDTO> authenticate(String username, String password) {
        AuthResponseDTO token = accessManagementFacade.login(username, password);
        if (token != null) {
            return ResponseEntity.ok().body(
                new AuthResponseDTO(username, token.id(), token.token(), token.expiresIn())
            );
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
