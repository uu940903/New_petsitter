package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MypageRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByMemberId(String memberid);

}
