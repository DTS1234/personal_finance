package integration;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateAssertion {
    public static void assertDateFormat(String actualDate, String expectedFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(expectedFormat);
        formatter.setLenient(false);
        try {
            formatter.parse(actualDate);
        } catch (ParseException e) {
            throw new AssertionError("Date " + actualDate + " is not in the expected format " + expectedFormat);
        }
    }
}
