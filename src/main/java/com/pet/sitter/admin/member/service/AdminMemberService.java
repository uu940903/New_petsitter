package com.pet.sitter.admin.member.service;

import com.pet.sitter.admin.exception.DataNotFoundException;
import com.pet.sitter.admin.member.repository.AdminMemberRepository;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;

    //회원목록조회
    public Page<MemberDTO> getMemberList(int page) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Member> memberPage = adminMemberRepository.findAll(pageable);

        List<MemberDTO> memberDTOList = memberPage.map(member -> {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setId(member.getId());
            memberDTO.setMemberId(member.getMemberId());
            memberDTO.setName(member.getName());
            memberDTO.setPhone(member.getPhone());
            memberDTO.setEMail(member.getEMail());
            memberDTO.setBirth(member.getBirth());
            memberDTO.setAddress(member.getAddress());
            memberDTO.setNickname(member.getNickname());
            memberDTO.setIsshow(member.getIsshow());
            return memberDTO;
        }).getContent();

        return new PageImpl<>(memberDTOList,pageable,memberPage.getTotalElements());
    }

    //회원 상세 조회
    public MemberDTO getMemberDetail(Long id){
        Optional<Member> optionalMember=adminMemberRepository.findById(id);
        if(!optionalMember.isPresent()){
            return null;
        }

        Member member = optionalMember.get();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setName(member.getName());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setBirth(member.getBirth());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setAddress(member.getAddress());
        memberDTO.setEMail(member.getEMail());
        return memberDTO;
    }

    //수정처리
    public void modify(Long id, String memberId, String name,
                       String nickname,String birth,String phone,String address,String eMail) {
        Optional<Member> memberOptional = adminMemberRepository.findById(id);
        if(memberOptional.isEmpty()){
            throw new DataNotFoundException("No Member");
        }
        Member member =memberOptional.get();
        member.setId(id);
        member.setMemberId(memberId);
        member.setName(name);
        member.setNickname(nickname);
        member.setBirth(birth);
        member.setPhone(phone);
        member.setAddress(address);
        member.setEMail(eMail);
        adminMemberRepository.save(member);
    }
    public Member getModify(Long id) {
        Optional<Member> member = adminMemberRepository.findById(id);
        if (!member.isPresent()) {
            throw new DataNotFoundException("오류났어요....");
        }
        return member.get();
    }

    //삭제처리
    @Transactional
    public void delete(Long id) {adminMemberRepository.deleteById(id);
    }




}
