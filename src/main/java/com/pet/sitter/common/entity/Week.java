package com.pet.sitter.common.entity;

import com.pet.sitter.mainboard.dto.WeekDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@IdClass(WeekId.class)
public class Week implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "sitterNo", referencedColumnName = "sitterNo")
    private Petsitter petsitter;

    @Id
    @Column
    private String day;

    @Builder
    public Week(Petsitter petsitter, String day) {
        this.petsitter = petsitter;
        this.day = day;
    }

    public static List<Week> dtoToEntity(WeekDTO weekDTO) {
        List<Week> weekList = new ArrayList<>();
        Week week = new Week();
        week.setDay(weekDTO.getDay());

        return weekList;
    }
}
