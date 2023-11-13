package personal.finance.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter
public class BigDecimalConverterQuantity implements AttributeConverter<BigDecimal, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            bigDecimal = bigDecimal.stripTrailingZeros();
            return bigDecimal;
        }
    }

    @Override
    public BigDecimal convertToEntityAttribute(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            bigDecimal = bigDecimal.stripTrailingZeros();
            if (bigDecimal.toString().contains("E")) {
                bigDecimal = BigDecimal.valueOf(Double.parseDouble(bigDecimal.stripTrailingZeros().toPlainString()));
            }
            return bigDecimal;
        }
    }

}
