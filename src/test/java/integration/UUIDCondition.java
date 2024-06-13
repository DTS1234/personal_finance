package integration;

import org.assertj.core.api.Condition;

import java.util.UUID;

public class UUIDCondition extends Condition<String> {
    public UUIDCondition() {
        super("a valid UUID");
    }

    @Override
    public boolean matches(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
