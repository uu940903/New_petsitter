package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MypageRepository2 extends JpaRepository<Petsitter,Long> {

    //내가쓴글가져오기
    Page<Petsitter> findByMemberMemberId(String name, Pageable pageable);

    //나의 매칭내역 가져오기


    @Query("SELECT p FROM Petsitter p JOIN Matching m ON p.sitterNo = m.petsitter.sitterNo WHERE m.member.id = :id")
    Page<Petsitter> findBytest(@Param("id") Long id,Pageable pageable);






}
