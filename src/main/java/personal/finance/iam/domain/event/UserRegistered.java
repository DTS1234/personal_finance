package personal.finance.iam.domain.event;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class UserRegistered extends ApplicationEvent {

    private UUID id;
    private String email;

    public UserRegistered(Object source, String email, UUID id) {
        super(source);
        this.email = email;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
