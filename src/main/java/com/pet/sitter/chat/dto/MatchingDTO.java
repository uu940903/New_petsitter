package com.pet.sitter.chat.dto;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MatchingDTO {

    private Petsitter petsitter;
    private LocalDateTime creatdateMatching;
    private Member member;
    private Member member2;
    private Long chatRoomNo;
}
