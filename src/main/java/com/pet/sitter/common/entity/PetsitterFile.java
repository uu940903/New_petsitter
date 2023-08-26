package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PetsitterFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer file_no;

    @Column
    @NotNull
    private String file_name;

    @Column
    @NotNull
    private String type;

    @ManyToOne
    @JoinColumn(name="sitter_no", referencedColumnName = "sitter_no")
    private Petsitter petsitter;
}
