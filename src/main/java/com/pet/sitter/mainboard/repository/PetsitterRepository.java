package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PetsitterRepository extends JpaRepository<Petsitter, Long> {

    //무한 스크롤
    Page<Petsitter> findAllBySitterNoLessThanOrderByPetRegdateDesc(Pageable page, Long sitterNo);
    //상세페이지 조회
    Petsitter findBySitterNo(Long sitterNo);

    Petsitter deleteBySitterNo (Long sitterNo);




}
