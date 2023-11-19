package personal.finance.iam.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {


    @EmbeddedId
    private UserId id;

    @Embedded
    private UserInformation userInformation;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VerificationToken> verificationTokens;

    public User enable() {
        this.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(this.userInformation.getPassword())
            .username(this.userInformation.getUsername())
            .email(this.userInformation.getEmail())
            .build());
        return this;
    }

    public boolean isEnabled() {
        return this.userInformation.isEnabled();
    }

    public User changePassword(String encodedPassword) {
        this.setUserInformation(UserInformation.builder()
            .enabled(true)
            .password(encodedPassword)
            .username(this.userInformation.getUsername())
            .email(this.userInformation.getEmail())
            .build());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
