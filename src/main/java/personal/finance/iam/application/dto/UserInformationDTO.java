package personal.finance.iam.application.dto;

import java.time.LocalDate;

public record UserInformationDTO(String firstname, String lastname, LocalDate birthdate, String gender, String email) {

}
