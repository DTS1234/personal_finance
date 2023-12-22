package personal.finance.summary.infrastracture.persistance.repository;

import org.springframework.stereotype.Component;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SummaryRepositorySql implements SummaryRepository {

    private final SummaryJpaRepository jpaRepository;
    private final DomainModelMapper mapper;

    public SummaryRepositorySql(SummaryJpaRepository jpaRepository, DomainModelMapper domainModelMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = domainModelMapper;
    }

    @Override
    public Summary save(Summary summary) {
        return mapper.map(jpaRepository.save(mapper.map(summary)));
    }

    @Override
    public List<Summary> saveAll(List<Summary> summaryList) {
        return jpaRepository.saveAll(mapToEntites(summaryList)).stream().map(mapper::map).collect(Collectors.toList());
    }

    private List<SummaryEntity> mapToEntites(List<Summary> entityList) {
        return entityList.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public Summary findByIdAndUserId(Long summaryId, UUID userId) {
        SummaryEntity entityFound = jpaRepository.findByIdAndUserId(summaryId, userId).orElse(null);
        if (entityFound == null) {
            return null;
        }
        return mapper.map(entityFound);
    }

    @Override
    public List<Summary> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, UUID userId) {
        List<SummaryEntity> summaryByStateEqualsAndUserIdOrderByDateDesc = jpaRepository
            .findSummaryByStateEqualsAndUserIdOrderByDateDesc(summaryState, userId);
        return summaryByStateEqualsAndUserIdOrderByDateDesc.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public List<Summary> findSummaryByUserIdAndState(UUID userId, SummaryState summaryState) {
        return jpaRepository.findSummaryByUserIdAndStateEquals(userId, summaryState).stream()
            .map(mapper::map)
            .toList();
    }
}
