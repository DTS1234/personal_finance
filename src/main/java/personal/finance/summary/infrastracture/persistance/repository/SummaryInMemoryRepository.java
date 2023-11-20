package personal.finance.summary.infrastracture.persistance.repository;

import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.SummaryId;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

public class SummaryInMemoryRepository implements SummaryRepository {

    private static final HashMap<Long, Summary> SUMMARIES = new HashMap<>();

    @Override
    public Summary save(Summary summary) {
        if (summary.getId() == null) {
            List<Long> ids = SUMMARIES.keySet().stream().sorted().toList();
            if (ids.isEmpty()) {
                summary.setId(new SummaryId(1L));
                SUMMARIES.put(1L, summary);
            } else {
                long newId = ids.stream().max(Long::compareTo).get() + 1;
                summary.setId(new SummaryId(newId));
                SUMMARIES.put(newId, summary);
            }
        } else {
            SUMMARIES.put(summary.getId().getValue(), summary);
        }
        return summary;
    }

    @Override
    public List<Summary> saveAll(List<Summary> entityList) {
        return entityList.stream().map(this::save).toList();
    }

    @Override
    public Summary findById(SummaryId id) {
        if (SUMMARIES.containsKey(id.getValue())) {
            return SUMMARIES.get(id.getValue());
        }
        return null;
    }

    @Override
    public Summary findByIdAndUserId(Long summaryId, UUID userId) {
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
