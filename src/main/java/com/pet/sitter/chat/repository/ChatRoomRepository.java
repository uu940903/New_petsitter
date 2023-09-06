package com.pet.sitter.chat.repository;

import com.pet.sitter.chat.dto.ChatRoomDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoomDTO> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoomDTO findRoomById(String id) {
        return chatRoomMap.get(id);
    }

    public ChatRoomDTO createChatRoom(String name) {
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(name);
        chatRoomMap.put(chatRoomDTO.getRoomId(), chatRoomDTO);
        return chatRoomDTO;
    }
}