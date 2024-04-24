package personal.finance.iam.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

import static personal.finance.iam.domain.SubscriptionType.PREMIUM;

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

    @OneToOne
    private UserSubscription userSubscription;

    public User enable() {
        UserInformation enabled = this.getUserInformation().enable();
        this.setUserInformation(enabled);
        return this;
    }

    public boolean isEnabled() {
        return this.userInformation.isEnabled();
    }

    public User changePassword(String encodedPassword) {
        UserInformation newUserInfo = this.getUserInformation().changePassword(encodedPassword);
        this.setUserInformation(newUserInfo);
        return this;
    }

    public User updateUserInfo(UserInformation userInformation) {
        this.setUserInformation(userInformation);
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

    public static User create() {
        User user = new User();
        user.id = UserId.random();
        return user;
    }

    public boolean isPremium() {
        return this.userSubscription != null
            && userSubscription.getSubscriptionType() == PREMIUM
            && userSubscription.isActive();
    }
}
