package com.pet.sitter.member.repository;

import com.pet.sitter.common.entity.Member;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBymemberId(String memberId);

    Member findMemberByMemberId(String memberId);

    //중복가입
    boolean existsByMemberId(String memberId);

    Optional<Member> findByPhone(String phone);

    Member findByNickname(String sender);
}
