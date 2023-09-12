package com.pet.sitter.member.repository;

import com.pet.sitter.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMemberId(String memberId);
}
