package integration;

import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.util.CanIgnoreReturnValue;

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class SummaryAssert extends AbstractAssert<SummaryAssert, String> {

    private final List<String> ignoredParams = new ArrayList<>(List.of(
        "date", "id"
    ));

    private SummaryAssert(String actual) {
        super(actual,  SummaryAssert.class);
    }

    static SummaryAssert assertThatSummary(String actual) {
        return new SummaryAssert(actual);
    }

    @CanIgnoreReturnValue
    SummaryAssert idIsUUID() {
        Condition<String> uuidCondition = new UUIDCondition();
        String id = JsonPath.read(actual, "$.id");
        assertThat(id)
            .is(uuidCondition);
        return this;
    }

    @CanIgnoreReturnValue
    SummaryAssert isEqualTo(String value) {
        assertThatJson(actual)
            .whenIgnoringPaths(ignoredParams.toArray(new String[0]))
            .isEqualTo(value);
        return this;
    }

    @CanIgnoreReturnValue
    SummaryAssert dateIsFormatted() {
        String actualDate = JsonPath.read(actual, "$.date");
        DateAssertion.assertDateFormat(actualDate, "dd.MM.yyyy HH:mm:ss");
        return this;
    }
}
