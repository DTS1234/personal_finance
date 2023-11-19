package personal.finance.iam.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

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

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

}
