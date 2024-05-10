package personal.finance.tracking.asset.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class ItemId {
    UUID value;

    public static ItemId random() {
        return new ItemId(UUID.randomUUID());
    }

}
