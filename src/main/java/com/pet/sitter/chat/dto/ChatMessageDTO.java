package com.pet.sitter.chat.dto;

import com.pet.sitter.common.entity.ChatMessage;
import com.pet.sitter.member.dto.MemberDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private ChatRoomDTO chatRoom; // 채팅방
    private MemberDTO sender; // 메시지 보낸사람
    private String content; // 메시지
    private LocalDateTime sendTime;//보낸시간


    @Builder
    public ChatMessageDTO (ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.sender = MemberDTO.builder().member(chatMessage.getSender()).build();
        this.content = chatMessage.getContent();
        this.sendTime = chatMessage.getSendTime();
    }

    public ChatMessageDTO(Long id, ChatRoomDTO chatRoom, MemberDTO sender, String content, LocalDateTime sendTime) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.sendTime = sendTime;
    }

}