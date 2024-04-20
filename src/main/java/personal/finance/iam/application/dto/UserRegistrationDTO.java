package personal.finance.iam.application.dto;

import java.time.LocalDate;

public record UserRegistrationDTO(String email,
                                  String password,
                                  LocalDate birthdate,
                                  String gender,
                                  String lastname,
                                  String firstname) {

    public UserRegistrationDTO(String email, String password) {
        this(email, password, null, null, null, null);
    }
}
