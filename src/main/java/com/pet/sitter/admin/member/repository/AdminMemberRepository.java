package com.pet.sitter.admin.member.repository;

import com.pet.sitter.common.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminMemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByName(String name);
    Optional<Member> findByPhone(String phone);
    Optional<Member> findByeMail(String eMail);
    Optional<Member> findByBirth(String birth);
    Optional<Member> findByAddress(String address);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByIsshow(String isshow);

    Optional<Member> findByMemberIdAndNickname(String memberId, String nickname);

    List<Member> findByMemberIdLike(String memberId);

    List<Member> findByNameLike(String name);
    Page<Member> findAll(Pageable pageable);


}
