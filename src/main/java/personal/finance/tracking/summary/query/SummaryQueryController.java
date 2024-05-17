package personal.finance.tracking.summary.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.tracking.summary.application.CurrencyManager;
import personal.finance.tracking.summary.application.dto.CurrenciesDTO;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.domain.UserRepository;
import personal.finance.tracking.summary.application.dto.SummaryDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/summaries/{summaryId}")
    public SummaryDTO findSummary(@PathVariable("summaryId") UUID summaryId) {
        return projection.findById(summaryId);
    }

    @GetMapping("/{userId}/currency")
    public String getCurrency(@PathVariable("userId") String userId) {
        return userRepository.getCurrency(UUID.fromString(userId)).name();
    }

    @GetMapping("/currencies")
    public CurrenciesDTO currencies() {
        return new CurrenciesDTO(CurrencyManager.currencies);
    }

    @GetMapping("/{userId}/summaries/current")
    public SummaryDTO getCurrentDraft(@PathVariable("userId") UUID userId) {
        return projection.getCurrentDraft(userId);
    }

    @GetMapping("/{userId}/summaries")
    public List<SummaryDTO> getSummaries(@PathVariable("userId") UUID userId) {
        return projection.getConfirmedSummaries(userId);
    }
}
