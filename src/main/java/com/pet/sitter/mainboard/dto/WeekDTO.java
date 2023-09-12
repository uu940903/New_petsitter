package com.pet.sitter.mainboard.dto;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeekDTO {

    private Long sitterNo;
    private String day;

    @Builder
    public WeekDTO(Week week) {
        this.sitterNo = week.getPetsitter().getSitterNo();
        this.day = week.getDay();
    }

    public Week toEntity() {
        return Week.builder()
                .petsitter(Petsitter.builder().sitterNo(this.sitterNo).build())
                .day(this.day)
                .build();
    }
}
