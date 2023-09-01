package com.pet.sitter.mypage.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.mypage.repository.MypageRepository;

import com.pet.sitter.mypage.repository.MypageRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MypageRepository mypageRepository;
    private final MypageRepository2 mypageRepository2;
    public MemberDTO getMember(String memberId) {
        Optional<Member> member = mypageRepository.findByMemberId(memberId);
        if (member.isPresent()) {
        MemberDTO memberDTO = new MemberDTO(member.get());
        return memberDTO;
        }else{
            throw new DataNotFoundException("member not Found");
        }
    }

    /*public Page<Petsitter> getMyArticleList(String id , int page){
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));
        Page<Petsitter> petsitterPage = mypageRepository2.findByMemberMemberId(id,pageable);
        Page<PetSitterDTO> petSitterDTOPage;
        for(int i=0;i<=petsitterPage.getTotalElements();i++){

        }

        return mypageRepository2.findByMemberMemberId(id,pageable);
    }*/

    public Page<PetSitterDTO> getMyArticleList(String id , int page){
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));
        Page<Petsitter> petsitterPage = mypageRepository2.findByMemberMemberId(id,pageable);


        Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> {
            PetSitterDTO dto = new PetSitterDTO(petsitter);
            dto.setMember(petsitter.getMember()); // Member 설정
            return dto;
        });

        return petSitterDTOPage;

        /*Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> new PetSitterDTO(petsitter));

        return petSitterDTOPage;
*/

    }
}

 /*Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> {
            PetSitterDTO dto = new PetSitterDTO();
            dto.setMember(petsitter.getMember());

            return dto;
        });*/

//return petSitterDTOPage;

//return mypageRepository2.findByMemberMemberId(id,pageable);