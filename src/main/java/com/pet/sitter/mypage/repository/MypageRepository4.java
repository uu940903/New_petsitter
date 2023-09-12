package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MypageRepository4 extends JpaRepository<Petsitter,Long> {

    @Query("SELECT m FROM Matching m WHERE m.member.id = :id OR m.member2.id = :id")
    Page<Matching> findByMatching(@Param("id") Long id, Pageable pageable);

}

