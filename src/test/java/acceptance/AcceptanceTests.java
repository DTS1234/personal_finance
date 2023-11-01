package acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import personal.finance.PersonalFinanceApplication;
import personal.finance.summary.persistance.SummarySQLRepository;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {PersonalFinanceApplication.class}
)
class AcceptanceTests {

    public static final String BASE_PATH = "http://localhost:%s";

    @LocalServerPort
    private int port;

    @Autowired
    private SummarySQLRepository summarySQLRepository;

}
