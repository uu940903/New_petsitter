package com.pet.sitter.mypage.dto;

import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MatchingDTO {

    private Petsitter petsitter;


    private Date creatdateMatching;


    private Member member;


    private Member member2;

    @Builder
    public MatchingDTO(Matching matching){
        this.petsitter=matching.getPetsitter();
        this.creatdateMatching=matching.getCreatdateMatching();
        this.member=matching.getMember();
        this.member2=matching.getMember2();
    }
}
