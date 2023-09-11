package com.pet.sitter.chat.repository;

import com.pet.sitter.chat.dto.ChatRoomDTO;
import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findChatRoomById(Long id);

    ChatRoom findChatRoomByRoomUUID(String roomUUID);

    ChatRoom findChatRoomByPetsitter(Petsitter petsitter);

    ChatRoom findChatRoomByPetsitterAndGuestId(Petsitter petsitter, String memberId);
}
