package com.pet.sitter.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingId implements Serializable {
    private Long petsitter;
    private LocalDateTime creatdateMatching;
    private Member member;
    private Member member2;
}
