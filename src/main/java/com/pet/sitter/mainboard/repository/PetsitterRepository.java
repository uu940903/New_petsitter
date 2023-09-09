package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PetsitterRepository extends JpaRepository<Petsitter, Long>, JpaSpecificationExecutor<Petsitter> {

    //페이지네이션
    Page<Petsitter> findAll(Pageable pageable);

    //무한 스크롤
    Page<Petsitter> findAllBySitterNoLessThanOrderByPetRegdateDesc(Pageable page, Long sitterNo);

    //상세페이지 조회
    Petsitter findBySitterNo(Long sitterNo);

    //검색
    Page<Petsitter> findAll(Specification<Petsitter> spec, Pageable pageable);



    //*****************혜지
    //삭제
    Petsitter deleteBySitterNo (Long sitterNo);

    //조회수
    @Modifying
    @Query ("update Petsitter p set p.petViewCnt = p.petViewCnt + 1 where p.sitterNo = :sitterNo")
    int updateViews(@Param("sitterNo") Long no);


    //좋아요
    @Modifying
    @Query ("update Petsitter p set p.likeCnt = p.likeCnt + 1 where p.sitterNo = :sitterNo")
    int updateLike (@Param("sitterNo") Long sitterNo);

/*    //제목으로 검색
    List<Petsitter> findByPetTitleContaining (String keyword);*/

    //제목 페이징 정렬검색
    Page<Petsitter> findByPetTitleContaining (Pageable pageable, String keyword);

}
