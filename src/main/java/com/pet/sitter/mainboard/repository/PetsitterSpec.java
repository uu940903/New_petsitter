package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PetsitterSpec {

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
            if (dayList!=null) {
                predicates.add(builder.equal(root.get("weekList"), dayList));
            }
            if (startTimeHour!=0 || endTimeHour!=0) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("startTimeHour"), startTimeHour));
                predicates.add(builder.lessThanOrEqualTo(root.get("endTimeHour"), endTimeHour));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
