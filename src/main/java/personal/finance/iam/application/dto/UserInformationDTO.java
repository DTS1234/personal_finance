package personal.finance.iam.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import personal.finance.iam.domain.UserInformation;

import java.time.LocalDate;

public record UserInformationDTO(String firstname, String lastname, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthdate, String gender, String email) {

    public static UserInformationDTO from(UserInformation userInformation) {
        return new UserInformationDTO(userInformation.getFirstname(), userInformation.getLastname(), userInformation.getBirthdate(),
            userInformation.getGender(), userInformation.getEmail());
    }

}
