package personal.finance.summary.infrastracture.persistance.repository;

import org.springframework.stereotype.Component;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SummaryRepositorySql implements SummaryRepository {

    private final SummaryJpaRepository jpaRepository;
    private final DomainModelMapper mapper;

    public SummaryRepositorySql(SummaryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
        this.mapper = new DomainModelMapper();
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
    public Summary findById(SummaryId id) {
        SummaryEntity summaryEntityFound = jpaRepository.findById(id.getValue()).orElse(null);
        if (summaryEntityFound == null) {
            return null;
        }
        return mapper.map(summaryEntityFound);
    }

    @Override
    public Summary findByIdAndUserId(Long summaryId, Long userId) {
        SummaryEntity entityFound = jpaRepository.findByIdAndUserId(summaryId, userId).orElse(null);
        if (entityFound == null) {
            return null;
        }
        return mapper.map(entityFound);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public List<Summary> findSummaryByStateEqualsOrderById(SummaryState summaryState) {
        List<SummaryEntity> summaryByStateEqualsOrderById = jpaRepository.findSummaryByStateEqualsOrderById(
            summaryState);
        return summaryByStateEqualsOrderById.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public List<Summary> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState) {
        List<SummaryEntity> summaryByStateEqualsOrderByDateDesc = jpaRepository.findSummaryByStateEqualsOrderByDateDesc(
            summaryState);
        return summaryByStateEqualsOrderByDateDesc
            .stream()
            .map(mapper::map)
            .collect(Collectors.toList());
    }

    @Override
    public List<Summary> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, Long userId) {
        List<SummaryEntity> summaryByStateEqualsAndUserIdOrderByDateDesc = jpaRepository
            .findSummaryByStateEqualsAndUserIdOrderByDateDesc(summaryState, userId);
        return summaryByStateEqualsAndUserIdOrderByDateDesc.stream().map(mapper::map).collect(Collectors.toList());
    }
}
