package com.pet.sitter.chat.controller;

import com.pet.sitter.chat.dto.ChatMessageDTO;
import com.pet.sitter.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        chatMessageService.sendMessage(message);
    }

    @MessageMapping("/chat/message/enter")
    public void enterMessage(Long chatRoomId, String memberId) {
        chatMessageService.enterMessage(chatRoomId, memberId);
    }
}