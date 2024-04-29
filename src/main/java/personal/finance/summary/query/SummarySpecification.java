package personal.finance.summary.application.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SummarySpecification implements Specification<SummaryEntity> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<SummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getUserId() != null) {
            predicates.add(builder.equal(root.get("userId"), criteria.getUserId()));
        }
        if (criteria.getMinMoneyValue() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("moneyValue"), criteria.getMinMoneyValue()));
        }
        if (criteria.getMaxMoneyValue() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("moneyValue"), criteria.getMaxMoneyValue()));
        }
        if (criteria.getStartDate() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("date"), criteria.getStartDate()));
        }
        if (criteria.getEndDate() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("date"), criteria.getEndDate()));
        }
        if (criteria.getState() != null) {
            predicates.add(builder.equal(root.get("state"), criteria.getState()));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
