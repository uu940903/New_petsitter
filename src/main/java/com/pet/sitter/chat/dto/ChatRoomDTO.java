package com.pet.sitter.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private Long roomNo;
    private String roomId;
    private String name;

    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDTO create(String name) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.roomId = UUID.randomUUID().toString();
        chatRoomDTO.name = name;

        return chatRoomDTO;
    }
}
