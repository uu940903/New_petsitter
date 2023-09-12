package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MypageRepository5 extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.petsitter.sitterNo = :id")
    Page<ChatRoom> findByChatRoom(@Param("id") Long id,Pageable pageable);
}
