package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Week {
    @Id
    @ManyToOne
    @JoinColumn(name="sitter_no", referencedColumnName = "sitter_no")
    private Petsitter petsitter;

    @Id
    @Column
    private String day;
}
