package com.pet.sitter.chat.controller;

import com.pet.sitter.chat.dto.ChatMessageDTO;
import com.pet.sitter.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void message(@Header("roomId") Long roomId, @Header("memberId") String memberId, @Payload ChatMessageDTO message) {
        System.out.println("messageContent(Controller): " + message.getContent());
        chatMessageService.sendMessage(roomId, memberId, message);
    }

    @MessageMapping("/chat/message/enter")
    public void enterMessage(Long chatRoomId, String memberId) {
        System.out.println("********1." + memberId + "********");
        chatMessageService.enterMessage(chatRoomId, memberId);
    }
}