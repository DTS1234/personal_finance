package personal.finance.iam.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.iam.application.dto.AuthResponseDTO;
import personal.finance.iam.application.dto.UserInformationDTO;
import personal.finance.iam.application.dto.UserRegistrationConfirmationDTO;
import personal.finance.iam.application.dto.UserRegistrationDTO;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserInformation;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(ReplaceUnderscores.class)
class UserServiceTest {

    private UserService userService;
    private AccessManagementFacade accessManagementFacade;

    @BeforeEach
    void setUp() {
        userService = new AccessManagementConfiguration().userServiceTest();
        accessManagementFacade = new AccessManagementConfiguration().accessManagementFacadeTest();
    }

    @Test
    void should_update_editable_user_info() {
        // given
        UserRegistrationConfirmationDTO confirmationDTO = accessManagementFacade.registerUser(
            new UserRegistrationDTO("email", "pass")
        );
        accessManagementFacade.confirmRegistration("mock token");
        AuthResponseDTO login = accessManagementFacade.login("email", "pass");

        // when
        UserInformationDTO userInformationDTO = new UserInformationDTO("Adam", "Kozak",
            LocalDate.of(2000, 10, 10), "Male", "emailNew");
        UserInformationDTO result = userService.updateUserInfo(
            new UserId(UUID.fromString(login.id())),
            userInformationDTO
        );

        // then
        assertThat(result.firstname()).isEqualTo("Adam");
        assertThat(result.lastname()).isEqualTo("Kozak");
        assertThat(result.birthdate()).isEqualTo(LocalDate.of(2000, 10, 10));
        assertThat(result.gender()).isEqualTo("Male");
        assertThat(result.email()).isEqualTo("email"); // should not be changed
    }

    @Test
    void should_throw_if_no_user_found() {
        UserInformationDTO userInformationDTO = new UserInformationDTO("Adam", "Kozak",
            LocalDate.of(2000, 10, 10), "Male", "emailNew");
        UserId unassignedId = UserId.random();
        assertThatThrownBy(() -> userService.updateUserInfo(unassignedId, userInformationDTO))
            .hasMessage("User not found. " + unassignedId);
    }
}