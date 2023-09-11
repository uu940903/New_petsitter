package com.pet.sitter.chat.repository;

import com.pet.sitter.common.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Matching findMatchingByChatRoomNo(Long roomId);
}
