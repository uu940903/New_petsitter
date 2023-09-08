package com.pet.sitter.chat.dto;

import com.pet.sitter.member.dto.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private MessageType type; // 메시지 타입
    private ChatRoomDTO chatRoom; // 채팅방
    private MemberDTO sender; // 메시지 보낸사람
    private String content; // 메시지
}