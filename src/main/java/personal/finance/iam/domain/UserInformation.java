package personal.finance.iam.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Builder
public class UserInformation implements Serializable {

    public UserInformation() {
    }

    public UserInformation(String email, String username, String password, boolean enabled) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public UserInformation(String email, String username, String password, LocalDate birthdate, String gender,
        String firstname, String lastname, boolean enabled) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.enabled = enabled;
    }

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private LocalDate birthdate;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String firstname;

    @Column(nullable = true)
    private String lastname;

    @Column(nullable = false)
    private boolean enabled;

    public UserInformation enable() {
        this.enabled = true;
        return this;
    }

    public UserInformation changePassword(String newPassword) {
        this.password = newPassword;
        return this;
    }
}
