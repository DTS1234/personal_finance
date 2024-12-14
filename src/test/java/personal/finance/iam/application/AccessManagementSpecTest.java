package personal.finance.iam.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import personal.finance.iam.application.dto.PasswordResetConfirmationDTO;
import personal.finance.iam.application.dto.PasswordResetState;
import personal.finance.iam.application.dto.RegistrationState;
import personal.finance.iam.application.dto.UserRegistrationConfirmationDTO;
import personal.finance.iam.application.dto.UserRegistrationDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccessManagementSpecTest {

    private AccessManagementFacade facade;

    @BeforeEach
    void setUp() {
        facade = new AccessManagementConfiguration().accessManagementFacadeTest();
    }

    @Test
    @DisplayName("should throw if unregistered user tries to login")
    void shouldThrowIfUnregisteredUserTriesToLogin() {
        assertThatThrownBy(() -> facade.login("does not exist", "password"))
            .hasMessage("Given credentials are incorrect.");
    }

    @Test
    @DisplayName("should throw if tries to register with existing email")
    void shouldThrowIfTriesToRegisterWithExistingEmail() {
        // given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        // when user register
        facade.registerUser(testRegistration);
        // and tries again
        assertThatThrownBy(() -> facade.registerUser(testRegistration))
            .hasMessage("User with this email address already exists");
    }

    @Test
    @DisplayName("should throw if tries to login without email confirmation")
    void shouldThrowIfTriesToLoginWithoutEmailConfirmed() {
        // given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        // when user register
        facade.registerUser(testRegistration);
        // and tries again
        assertThatThrownBy(() -> facade.login("user@user.com", "pass"))
            .hasMessage("User email address is not confirmed.");
    }

    @Test
    @DisplayName("should throw if password incorrect")
    void shouldThrowIfPasswordIncorrect() {
        // given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        // when user register and confirm
        facade.registerUser(testRegistration);
        facade.confirmRegistration("mock token");
        // and tries to log in
        assertThatThrownBy(() -> facade.login("user@user.com", "passwrong"))
            .hasMessage("Given credentials are incorrect.");
    }

    @Test
    @DisplayName("should throw if email incorrect")
    void shouldThrowIfEmailIncorrect() {
        // given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        // when user register and confirm
        facade.registerUser(testRegistration);
        facade.confirmRegistration("mock token");
        // and tries to log in
        assertThatThrownBy(() -> facade.login("userwrong@user.com", "pass"))
            .hasMessage("Given credentials are incorrect.");
    }

    @Test
    @DisplayName("should throw if confirmation token is wrong")
    void shouldReturnFailedConfirmationIfTokenIsWrong() {
        // given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        // when user register and confirm
        facade.registerUser(testRegistration);
        // then
        UserRegistrationConfirmationDTO confirmationDTO = facade.confirmRegistration("wrong token");
        Assertions.assertThat(confirmationDTO.registrationState()).isEqualTo(RegistrationState.FAILED);
    }

    @Test
    @DisplayName("should login if account registered and email confirmed")
    void shouldLoginIfAccountRegisteredAndEmailConfirmed() {
        // given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        // when user register and confirm
        facade.registerUser(testRegistration);
        facade.confirmRegistration("mock token");
        // then
        String result = facade.login("user@user.com", "pass").token();
        assertThat(result).isEqualTo("jwt mock token");
    }

    @Test
    @DisplayName("should throw if password reset request does not exist")
    void shouldThrowIfPasswordResetRequestEmailDoesNotExist() {
        //given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        facade.registerUser(testRegistration);
        // when
        assertThatThrownBy(() -> facade.requestPasswordReset("does@notexist.com"))
            .hasMessage("User with this email address does not exist.");
    }

    @Test
    @DisplayName("should start password reset process")
    void shouldStartPasswordResetProcess() {
        //given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        facade.registerUser(testRegistration);
        facade.confirmRegistration("mock token");
        // when
        PasswordResetConfirmationDTO passwordResetConfirmationDTO = facade.requestPasswordReset("user@user.com");
        // then
        assertThat(passwordResetConfirmationDTO.state()).isEqualTo(PasswordResetState.IN_PROGRESS);
    }

    @Test
    @DisplayName("should allow for password change")
    void shouldChangeThePassword() {
        //given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        facade.registerUser(testRegistration);
        facade.confirmRegistration("mock token");
        // when
        facade.requestPasswordReset("user@user.com");
        PasswordResetConfirmationDTO passwordResetConfirmationDTO = facade.resetPassword("mock token",  "newPass");
        // then
        assertThat(passwordResetConfirmationDTO.state()).isEqualTo(PasswordResetState.SUCCESS);
        String token = facade.login("user@user.com", "newPass").token();
        assertThat(token).isEqualTo("jwt mock token");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"wrong token"})
    @DisplayName("should throw if token does not match")
    void shouldThrowIfTokenDoesNotMatch(String token) {
        //given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        facade.registerUser(testRegistration);
        facade.confirmRegistration("mock token");

        // when
        facade.requestPasswordReset("user@user.com");
        // then
        assertThatThrownBy(() -> facade.resetPassword(token, "newPass"))
            .hasMessage("Reset password link is invalid.");
    }

    @Test
    @DisplayName("should throw if user not confirmed")
    void shouldThrowIfUserNotConfirmed() {
        //given
        UserRegistrationDTO testRegistration = new UserRegistrationDTO("user@user.com", "pass");
        facade.registerUser(testRegistration);

        // then
        assertThatThrownBy(() -> facade.requestPasswordReset("user@user.com"))
            .hasMessage("User email address is not confirmed.");
    }
}
