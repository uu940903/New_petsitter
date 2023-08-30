package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Matching {
    @Id
    @JoinColumn(name="sitterNo", referencedColumnName = "sitterNo")
    @OneToOne
    private Petsitter petsitter;

    @Column
    @NotNull
    private Date creatdateMatching;

    @ManyToOne
    @JoinColumn(name="matchingNo1", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="matchingNo2", referencedColumnName = "id")
    private Member member2;

}
