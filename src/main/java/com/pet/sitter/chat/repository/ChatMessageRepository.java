package com.pet.sitter.chat.repository;

import com.pet.sitter.common.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findChatMessagesByChatRoom_Id(Long roomId);

    List<ChatMessage> findChatMessagesByChatRoomRoomUUID(String roomUUID);

    List<ChatMessage> findChatMessagesBySender(String memberId);
}