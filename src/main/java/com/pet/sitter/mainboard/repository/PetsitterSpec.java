package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.Week;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PetsitterSpec {
    @Transactional
    public static Specification<Petsitter> searchWith(final String category, final String petCategory, final String petAddress, final List<String> dayList, final int startTimeHour, int endTimeHour) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(category)) {
                predicates.add(builder.equal(root.get("category"), category));
            }
            if(!petCategory.equals("all") && (StringUtils.hasText(petCategory))) {
                    predicates.add(builder.equal(root.get("petCategory"), petCategory));
            }
            if (StringUtils.hasText(petAddress)) {
                predicates.add(builder.like(root.get("petAddress"),"%" + petAddress + "%"));
            }
            if (dayList != null && !dayList.isEmpty()) {
                Subquery<Week> subquery = query.subquery(Week.class);
                Root<Week> weekRoot = subquery.from(Week.class);
                subquery.select(weekRoot);

                Join<Petsitter, Week> weekJoin = root.join("weekList", JoinType.INNER);
                subquery.where(builder.and(
                        builder.equal(weekJoin.get("petsitter"), root),
                        weekJoin.get("day").in(dayList)
                ));
                predicates.add(builder.exists(subquery));
            }
            if (startTimeHour != 0 && endTimeHour != 0) {
                Expression<Integer> startTimeExpression = builder.function("hour", Integer.class, root.get("startTime"));
                Expression<Integer> endTimeExpression = builder.function("hour", Integer.class, root.get("endTime"));

                predicates.add(builder.greaterThanOrEqualTo(startTimeExpression, startTimeHour));
                predicates.add(builder.lessThanOrEqualTo(endTimeExpression, endTimeHour));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Petsitter> recommendWith(String category, String petCategory, String sitterAddress) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(category)) {
                predicates.add(builder.equal(root.get("category"), category));
            }
            if (StringUtils.hasText(petCategory)) {
                predicates.add(builder.equal(root.get("petCategory"), petCategory));
            }
            if (StringUtils.hasText(sitterAddress)) {
                predicates.add(builder.like(root.get("petAddress"),"%" + sitterAddress + "%"));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
