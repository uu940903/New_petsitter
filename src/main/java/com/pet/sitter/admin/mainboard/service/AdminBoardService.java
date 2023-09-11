package com.pet.sitter.admin.mainboard.service;

import com.pet.sitter.admin.exception.DataNotFoundException;
import com.pet.sitter.admin.mainboard.repository.AdminBoardRepository;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;

    //페이징기능이 있는 게시글 조회
    public Page<PetSitterDTO> getBoardList(int page) {
        Sort sort = Sort.by(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Petsitter> petSitterPage = adminBoardRepository.findAll(pageable);

        List<PetSitterDTO> petSitterDTOList = petSitterPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = new PetSitterDTO();
            petSitterDTO.setSitterNo(petsitter.getSitterNo());
            petSitterDTO.setPetTitle(petsitter.getPetTitle());
            petSitterDTO.setPetContent(petsitter.getPetContent());
            petSitterDTO.setCategory(petsitter.getCategory());
            petSitterDTO.setMember(MemberDTO.builder().member(petsitter.getMember()).build());
            petSitterDTO.setPetCategory(petsitter.getPetCategory());
            petSitterDTO.setPetRegdate(petsitter.getPetRegdate());
            petSitterDTO.setPetViewCnt(petsitter.getPetViewCnt());
            petSitterDTO.setStartTime(petsitter.getStartTime());
            petSitterDTO.setEndTime(petsitter.getEndTime());
            return petSitterDTO;
        }).getContent();
        return new PageImpl<>(petSitterDTOList, pageable, petSitterPage.getTotalElements());
    }

    //게시글 상세 조회
    public PetSitterDTO getBoardDetail(Long sitterNo) {
        Optional<Petsitter> optionalPetsitter = adminBoardRepository.findBySitterNo(sitterNo);
        if (!optionalPetsitter.isPresent()) {
            return null; // 해당하는 경우에 맞게 처리하세요
        }

        Petsitter petsitter = optionalPetsitter.get();
        PetSitterDTO petSitterDTO = new PetSitterDTO();
        petSitterDTO.setSitterNo(sitterNo);
        petSitterDTO.setPetTitle(petsitter.getPetTitle());
        petSitterDTO.setPetContent(petsitter.getPetContent());
        petSitterDTO.setPetRegdate(petsitter.getPetRegdate());

        Member member = petsitter.getMember();
        MemberDTO memberDTO = new MemberDTO(member);
        petSitterDTO.setMember(memberDTO);

        if (!petsitter.getPetsitterFileList().isEmpty()) {
            List<PetSitterFileDTO> petSitterFileDTOList = new ArrayList<>();
            for (int i = 0; i < petsitter.getPetsitterFileList().size(); i++) {
                PetSitterFileDTO petSitterFileDTO = PetSitterFileDTO.builder().petsitterFile(petsitter.getPetsitterFileList().get(i)).build();
                petSitterFileDTOList.add(petSitterFileDTO);
            }
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
        }

        System.out.println("파일 출력 = " + petSitterDTO.getFileDTOList());
        return petSitterDTO;
    }

    //수정처리
    public void modify(Petsitter petsitter, String petTitle, String petContent) {
        System.out.println("modify 서비스 진입");
        System.out.println(petsitter.toString());
        petsitter.setPetTitle(petTitle);
        petsitter.setPetContent(petContent);
        adminBoardRepository.save(petsitter);
    }

    public Petsitter getModify(Long sitterNo) {
        Optional<Petsitter> petsitter = adminBoardRepository.findBySitterNo(sitterNo);
        if (!petsitter.isPresent()) {
            throw new DataNotFoundException("오류났어요....");
        }
        return petsitter.get();
    }

    //삭제처리
    @Transactional
    public void delete(Long sitterNo) {
        adminBoardRepository.deleteById(sitterNo);
    }
    
}
