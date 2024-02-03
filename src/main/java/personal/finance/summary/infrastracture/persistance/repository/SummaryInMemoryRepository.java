package personal.finance.summary.infrastracture.persistance.repository;

import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

public class SummaryInMemoryRepository implements SummaryRepository {

    private static final HashMap<UUID, Summary> SUMMARIES = new HashMap<>();

    @Override
    public Summary save(Summary summary) {
        if (summary.getId() == null) {
            SummaryId id = SummaryId.random();
            summary.setId(id);
            SUMMARIES.put(id.getValue(), summary);
        } else {
            SUMMARIES.put(summary.getId().getValue(), summary);
        }
        return summary;
    }

    @Override
    public List<Summary> saveAll(List<Summary> entityList) {
        return entityList.stream()
            .map(this::save).toList();
    }

    @Override
    public Summary findByIdAndUserId(UUID summaryId, UUID userId) {
        if (SUMMARIES.containsKey(summaryId)) {
            if (SUMMARIES.get(summaryId).getUserId().equals(userId)) {
                return SUMMARIES.get(summaryId);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Summary> findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState summaryState, UUID userId) {
        return SUMMARIES.entrySet().stream()
            .filter(e -> e.getValue().getState().equals(summaryState))
            .filter(e -> e.getValue().getUserId().equals(userId))
            .sorted(Comparator.comparing(e -> e.getValue().getDate()))
            .map(Entry::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public List<Summary> findSummaryByUserIdAndState(UUID userId, SummaryState summaryState) {
        return SUMMARIES.values().stream()
            .filter(summary -> summary.getState().equals(summaryState))
            .filter(summary -> summary.getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    public SummaryInMemoryRepository clear() {
        SUMMARIES.clear();
        return this;
    }
}
