package personal.finance.summary.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import personal.finance.summary.application.dto.DTOMapper;
import personal.finance.summary.application.dto.SummaryDTO;
import personal.finance.summary.infrastracture.persistance.repository.SummaryJpaRepository;

@RequiredArgsConstructor
@Component
public class SummaryProjection {

    private final SummaryJpaRepository jpaRepository;

    public Page<SummaryDTO> query(SearchCriteria searchCriteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(new SummarySpecification(searchCriteria), pageable)
            .map(DTOMapper::dto);
    }
}
