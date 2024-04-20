package personal.finance.iam.application.dto;

import personal.finance.iam.domain.UserInformation;

public record UserRegistrationConfirmationDTO(String message, RegistrationState registrationState, UserInformation userInformation) {

}

