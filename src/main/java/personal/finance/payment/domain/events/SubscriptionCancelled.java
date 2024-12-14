package personal.finance.payment.domain.events;

import org.springframework.context.ApplicationEvent;
import personal.finance.common.events.Event;

import java.time.LocalDateTime;

public class SubscriptionCancelled extends ApplicationEvent implements Event {

    public String userId;
    public LocalDateTime expiresAt;

    public SubscriptionCancelled(Object source, String userId, LocalDateTime expiresAt) {
        super(source);
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        SubscriptionCancelled that = (SubscriptionCancelled) object;
        return userId.equals(that.userId) && expiresAt.equals(that.expiresAt);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + expiresAt.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SubscriptionCancelled{" +
            "userId='" + userId + '\'' +
            ", expiresAt=" + expiresAt +
            '}';
    }
}
