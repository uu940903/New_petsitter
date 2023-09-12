package com.pet.sitter.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingNo;

    @Column
    @NotNull
    private LocalDateTime creatdateMatching;

    @ManyToOne
    @JoinColumn(name = "matchingNo1", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "matchingNo2", referencedColumnName = "id")
    private Member member2;

    @OneToOne
    @JoinColumn(name = "sitter_no", referencedColumnName = "sitterNo")
    private Petsitter petsitter;

    @Column
    private Long chatRoomNo;
}