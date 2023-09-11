package com.pet.sitter.admin.mainboard.repository;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdminBoardRepository extends JpaRepository<Petsitter,Long> {
    Optional<Petsitter> findBySitterNo(Long sitterNo);
    Optional<Petsitter> findByPetTitle(String petTitle);
    Optional<Petsitter> findByPetContent(String petContent);
    Optional<Petsitter> findByCategory(String category);
    Optional<Petsitter> findByPetRegdate(LocalDateTime petRegdate);
    Optional<Petsitter> findByPetViewCnt(Integer petViewCnt);
    Optional<Petsitter> findByPrice(Integer price);
    Optional<Petsitter> findByPetCategory(String petCategory);
    Optional<Petsitter> findByStartTime(LocalDateTime petstartTime);
    Optional<Petsitter> findByEndTime(LocalDateTime endTime);
    Optional<Petsitter> findByPetAddress(String petAddress);
    Optional<Petsitter> findByPetTitleAndPetContent(String petTitle, String petContent);
    Optional<Petsitter> findByMember(Member member);
    List<Petsitter> findByPetTitleLike(String petTitle);
    Optional<Petsitter> findAllBySitterNo(Long sitterNo);
    Page<Petsitter> findAll(Pageable pageable);
    @Query("SELECT p FROM Petsitter p JOIN FETCH p.member m WHERE p.sitterNo = :sitterNo")
    Optional<Petsitter> findBySitterNoWithMember(@Param("sitterNo") Long sitterNo);

}
