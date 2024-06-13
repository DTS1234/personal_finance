package personal.finance.tracking.asset.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import personal.finance.tracking.summary.domain.Money;

import java.util.UUID;

@Setter
@Getter
@SuperBuilder
public abstract class Item {

    @NonNull
    protected ItemId id;
    protected Money money;
    @NonNull
    protected String name;

    public Item(@NonNull ItemId id, Money money, @NonNull String name) {
        this.id = id;
        this.money = money;
        this.name = name;
    }

    public UUID getIdValue() {
        return id.getValue();
    }

    public abstract Item newCopyForAsset();
}
