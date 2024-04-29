package personal.finance.summary.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.summary.application.CurrencyManager;
import personal.finance.summary.application.dto.CurrenciesDTO;
import personal.finance.summary.domain.UserRepository;
import personal.finance.summary.application.dto.SummaryDTO;

import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class SummaryQueryController {

    private final SummaryProjection projection;
    private final UserRepository userRepository;

    @PostMapping("/summaries")
    public Page<SummaryDTO> summaryQuery(
        @RequestBody SearchCriteria searchCriteria,
        @RequestParam(defaultValue = "0", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size) {
        return projection.query(searchCriteria, page, size);
    }

    @GetMapping("/{userId}/currency")
    public String getCurrency(@PathVariable("userId") String userId) {
        return userRepository.getCurrency(UUID.fromString(userId)).name();
    }

    @GetMapping("/currencies")
    public CurrenciesDTO currencies() {
        return new CurrenciesDTO(CurrencyManager.currencies);
    }
}
