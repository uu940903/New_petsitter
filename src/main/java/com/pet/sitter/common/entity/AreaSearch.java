package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreaSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petAddressNo;

    @Column
    @NotNull
    private String gu;

    @Column
    @NotNull
    private String roadname;

    @Column
    @NotNull
    private String detailAddress;

    @Column
    @NotNull
    private String post;

    @OneToMany(mappedBy = "areaSearch", cascade = CascadeType.REMOVE)
    private List<Petsitter> petsitterList;

    @Builder

    public AreaSearch(Long petAddressNo, String gu, String roadname, String detailAddress, String post) {
        this.petAddressNo = petAddressNo;
        this.gu = gu;
        this.roadname = roadname;
        this.detailAddress = detailAddress;
        this.post = post;
    }
}
