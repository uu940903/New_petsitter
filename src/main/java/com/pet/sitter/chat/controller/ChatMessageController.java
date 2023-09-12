package com.pet.sitter.chat.controller;

import com.pet.sitter.chat.dto.ChatMessageDTO;
import com.pet.sitter.chat.service.ChatMessageService;
import com.pet.sitter.chat.service.ChatRoomService;
import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageDTO chatMessageDTO) {
        chatMessageService.sendMessage(chatMessageDTO.getChatRoom().getId(),
                chatMessageDTO.getSender().getMemberId(),
                chatMessageDTO);
    }

    @MessageMapping("/chat/message/enter")
    public void enterMessage(Long chatRoomId, String memberId) {
        chatMessageService.enterMessage(chatRoomId, memberId);
    }

    @GetMapping("/chat/history/{roomUUID}")
    public List<ChatMessageDTO> loadPreviousMessages(@PathVariable String roomUUID) {
        return chatMessageService.getPreviousMessages(roomUUID);
    }


    //매칭 테이블
    @GetMapping("/chat/room/matching")
    public void matching(Principal principal, @RequestParam Map<String, Object> map) {
        System.out.println("matchingController 접근");
        System.out.println("roomUUID: " + map.get("roomUUID"));
        ChatRoom chatRoom = chatRoomService.getChatRoomByRoomUUID((String) map.get("roomUUID"));
        Long roomId = chatRoom.getId();
        String hostId = chatRoom.getHostId();
        String guestId = chatRoom.getGuestId();
        String roomUUID = chatRoom.getRoomUUID();
        if (!principal.getName().equals(hostId)) {
            throw new DataNotFoundException("hostId가 아닙니다.");
        }
        chatMessageService.matching(roomId, hostId, guestId);
    }

    @GetMapping("/chat/room/delmatching")
    public void delMatching(Principal principal, @RequestParam Map<String, Object> map){
        chatMessageService.delMatching(chatRoomService.getChatRoomByRoomUUID((String) map.get("roomUUID")).getId());
    }
}