package personal.finance.payment.domain.events;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationEvent;
import personal.finance.common.events.Event;
import personal.finance.iam.domain.SubscriptionType;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionCreated extends ApplicationEvent implements Event {

    public LocalDate start;
    public LocalDate expiresAt;
    public SubscriptionType type;
    public UUID userId;

    public SubscriptionCreated(Object source, SubscriptionType type, UUID userId, @NotNull LocalDate expiresAt) {
        super(source);
        this.start = LocalDate.now();
        this.expiresAt = expiresAt;
        this.type = type;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        SubscriptionCreated that = (SubscriptionCreated) object;
        return start.equals(that.start) && expiresAt.equals(that.expiresAt) && type == that.type && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + expiresAt.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
