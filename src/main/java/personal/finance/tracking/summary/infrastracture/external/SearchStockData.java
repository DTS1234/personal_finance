package personal.finance.tracking.summary.infrastracture.external;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SearchStockData {

       @JsonProperty("Code")
       private String code;

       @JsonProperty("Name")
       private String name;

       @JsonProperty("Type")
       private String type;

       @JsonProperty("Country")
       private String country;

       @JsonProperty("Currency")
       private String currency;

       @JsonProperty("ISIN")
       private String isin;

       @JsonProperty("previousClose")
       private BigDecimal previousClose;

       @JsonProperty("previousCloseDate")
       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
       private LocalDate previousCloseDate;
}

