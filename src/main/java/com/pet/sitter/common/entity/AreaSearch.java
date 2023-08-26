package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class AreaSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pet_address_no;

    @Column
    @NotNull
    private String gu;

    @Column
    @NotNull
    private String roadname;

    @Column
    @NotNull
    private String detail_address;

    @Column
    @NotNull
    private String post;

    @OneToMany(mappedBy = "areaSearch", cascade = CascadeType.REMOVE)
    private List<Petsitter> petsitterList;

}
