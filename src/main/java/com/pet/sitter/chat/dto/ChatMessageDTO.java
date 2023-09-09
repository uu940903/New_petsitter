package com.pet.sitter.chat.dto;

import com.pet.sitter.member.dto.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private ChatRoomDTO chatRoom; // 채팅방
    private MemberDTO sender; // 메시지 보낸사람
    private String content; // 메시지
    private LocalDateTime sendTime;

    public ChatMessageDTO(Long id, ChatRoomDTO chatRoom, MemberDTO sender, String content, LocalDateTime sendTime) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.sendTime = sendTime;
    }
}