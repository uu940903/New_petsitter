package com.pet.sitter.mypage.dto;

import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.member.dto.MemberDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class MatchingDTO {

    private Petsitter petsitter;
    private LocalDateTime creatdateMatching;
    private MemberDTO member;
    private MemberDTO member2;

    @Builder
    public MatchingDTO(Matching matching){
        this.petsitter=matching.getPetsitter();
        this.creatdateMatching=matching.getCreatdateMatching();

    }

}
