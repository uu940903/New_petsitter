package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


public interface PetsitterRepository extends JpaRepository<Petsitter, Long>, JpaSpecificationExecutor<Petsitter> {

    //페이지네이션
    Page<Petsitter> findAll(Pageable pageable);
    Page<Petsitter> findAllByPetAddressContaining(Pageable pageable, String petAddress);

    //무한 스크롤
    //Page<Petsitter> findAllBySitterNoLessThanOrderByPetRegdateDesc(Pageable page, Long sitterNo);

    //상세페이지 조회
    Petsitter findBySitterNo(Long sitterNo);

    //카테고리 검색
    Page<Petsitter> findAll(Specification<Petsitter> spec, Pageable pageable);

    //추천
    //List<Petsitter> findByPetCategoryAndPetAddressOrderByPetRegdateDesc(String petCategory, String petAddress);

    Petsitter deleteBySitterNo (Long sitterNo);

}
