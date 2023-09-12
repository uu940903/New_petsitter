package com.pet.sitter.chat.dto;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import lombok.*;

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
