package com.pet.sitter.mypage.dto;

import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.member.dto.MemberDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
