package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsitterRepository extends JpaRepository<Petsitter, Long>, JpaSpecificationExecutor<Petsitter> {

    //페이지네이션
    Page<Petsitter> findAll(Pageable pageable);
    //무한 스크롤
    Page<Petsitter> findAllBySitterNoLessThanOrderByPetRegdateDesc(Pageable page, Long sitterNo);
    //상세페이지 조회
    Petsitter findBySitterNo(Long sitterNo);
    //검색
   Page<Petsitter> findAll(Specification<Petsitter> spec, Pageable pageable);
    
}
