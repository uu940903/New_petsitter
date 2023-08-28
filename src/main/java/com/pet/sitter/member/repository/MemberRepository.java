package com.pet.sitter.member.repository;

import com.pet.sitter.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByIdAndPw(String id, String pw);


}
