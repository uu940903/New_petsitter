package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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