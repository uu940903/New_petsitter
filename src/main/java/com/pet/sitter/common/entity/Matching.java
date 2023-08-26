package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Matching {
    @Id
    @JoinColumn(name="sitter_no", referencedColumnName = "sitter_no")
    @OneToOne
    private Petsitter petsitter;

    @Column
    @NotNull
    private Date creatdate_m;

    @ManyToOne
    @JoinColumn(name="matching_id1", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="matching_id2", referencedColumnName = "id")
    private Member member2;

}
