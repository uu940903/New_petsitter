package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Week {
    @Id
    @ManyToOne
    @JoinColumn(name="sitterNo", referencedColumnName = "sitterNo")
    private Petsitter petsitter;

    @Id
    @Column
    private String day;

    @Builder
    public Week(Petsitter petsitter, String day) {
        this.petsitter = petsitter;
        this.day = day;
    }
}
