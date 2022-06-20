package personal.finance;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class SummaryController {

    private final SummaryRepository summaryRepository;

    @GetMapping("/summaries")
    public List<Summary> getSummaries() {
        return summaryRepository.findAll();
    }

}
