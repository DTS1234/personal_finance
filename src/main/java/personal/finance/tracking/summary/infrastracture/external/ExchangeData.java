package personal.finance.tracking.summary.infrastracture.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class ExchangeData {

    @Setter
    @Id
    @Getter
    private UUID id;
    public String Name;
    @JsonProperty("Code")
    public String code;
    public String OperatingMIC;
    public String Country;
    public String Currency;
    public String CountryISO2;
    public String CountryISO3;

    public ExchangeData(String name, String code, String operatingMIC, String country, String currency,
        String countryISO2, String countryISO3) {
        this.id = UUID.randomUUID();
        Name = name;
        this.code = code;
        OperatingMIC = operatingMIC;
        Country = country;
        Currency = currency;
        CountryISO2 = countryISO2;
        CountryISO3 = countryISO3;
    }

    @OneToMany
    @JsonIgnore
    public Set<TickerData> tickers;
}
