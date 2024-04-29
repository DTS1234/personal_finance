package personal.finance.summary.infrastracture.external;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class TickerData {

    @Id
    @JsonIgnore
    @Getter
    @Setter
    private UUID id;
    public String Code;
    public String Name;
    public String Country;
    public String Exchange;
    public String Currency;
    public String Type;
    public String Isin;

    public TickerData(String code, String name, String country, String exchange, String currency, String type,
        String isin) {
        this.id = UUID.randomUUID();
        Code = code;
        Name = name;
        Country = country;
        Exchange = exchange;
        Currency = currency;
        Type = type;
        Isin = isin;
    }

    @ManyToOne
    @JsonIgnore
    public ExchangeData exchangeData;
}
