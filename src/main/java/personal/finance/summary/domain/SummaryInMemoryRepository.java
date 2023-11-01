package personal.finance.summary.domain;

import personal.finance.summary.domain.model.SummaryState;
import personal.finance.summary.domain.model.Summary;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class SummaryInMemoryRepository implements SummaryRepository {

    private final HashMap<Long, Summary> SUMMARIES = new HashMap<>();

    @Override
    public Summary save(Summary summary) {
        if (summary.getId() == null) {
            List<Long> ids = SUMMARIES.keySet().stream().sorted().toList();
            if (ids.isEmpty()) {
                summary.setId(1L);
                SUMMARIES.put(1L, summary);
            } else {
                long newId = ids.stream().max(Long::compareTo).get() + 1;
                summary.setId(newId);
                SUMMARIES.put(newId, summary);
            }
        } else {
            SUMMARIES.put(summary.getId(), summary);
        }
        return summary;
    }

    @Override
    public List<Summary> saveAll(List<Summary> entityList) {
        return entityList.stream().map(this::save).toList();
    }

    @Override
    public Optional<Summary> findById(Long id) {
        if (SUMMARIES.containsKey(id)) {
            return Optional.of(SUMMARIES.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        SUMMARIES.clear();
    }

    @Override
    public List<Summary> findSummaryByStateEqualsOrderById(SummaryState summaryState) {
        return SUMMARIES.entrySet().stream()
            .filter(e -> e.getValue().getState().equals(summaryState))
            .sorted(Comparator.comparing(e -> e.getValue().getId()))
            .map(Entry::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public List<Summary> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState) {
        return SUMMARIES.entrySet().stream()
            .filter(e -> e.getValue().getState().equals(summaryState))
            .sorted(Comparator.comparing(e -> e.getValue().getDate()))
            .map(Entry::getValue)
            .collect(Collectors.toList());
    }
}
