package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.ChatMessage;
import com.pet.sitter.common.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MypageRepository6 extends JpaRepository<ChatMessage,Long> {

   /* @Query("SELECT cm FROM ChatMessage cm JOIN ChatRoom cr ON cm.chatRoom.id = cr.id and cr.petsitter.sitterNo = :id")
    List<ChatMessage> findByChatMessage(@Param("id") Long id);*/

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :id")
    List<ChatMessage> findByChatMessage(@Param("id") Long id);
    /* select * from chat_message where chat_room_id = 6;*/
}
