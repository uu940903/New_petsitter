package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MypageRepository2 extends JpaRepository<Petsitter,Long> {


    Page<Petsitter> findByMemberMemberId(String name, Pageable pageable);
    //Member findby

}
