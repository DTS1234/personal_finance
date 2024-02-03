package personal.finance.summary.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class AssetId {
    UUID value;

    public static AssetId random() {
        return new AssetId(UUID.randomUUID());
    }
}
