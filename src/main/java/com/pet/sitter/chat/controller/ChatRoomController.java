package com.pet.sitter.chat.controller;

import com.pet.sitter.chat.dto.ChatRoomDTO;
import com.pet.sitter.chat.repository.ChatRoomRepository;
import com.pet.sitter.chat.service.ChatRoomService;
import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.mypage.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    private MypageService mypageService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomService.getAllChatRooms();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoomDTO createRoom(@RequestParam("petsitterNo") Long id, @RequestParam("host") String hostId, @RequestParam("guest") String guestId) {

        return chatRoomService.createChatRoom(id, hostId, guestId);
    }

    @GetMapping("/room/{roomUUID}")
    @ResponseBody
    public ChatRoomDTO roomInfo(@PathVariable String roomUUID) {
        return new ChatRoomDTO(chatRoomRepository.findChatRoomByRoomUUID(roomUUID));
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomUUID}")
    public String roomDetail(Model model, @PathVariable String roomUUID) {
        ChatRoom chatRoom = chatRoomService.getChatRoomByRoomUUID(roomUUID);
        System.out.println("rOOmUUID: " + roomUUID);
        Long roomId = chatRoomService.getChatRoomByRoomUUID(roomUUID).getId();
        Matching matching = chatRoomService.getMatchingByRoomId(roomId);

        if (chatRoom != null) {
            if (matching != null){
                model.addAttribute("matchChatRoomNo", matching.getChatRoomNo());
            }
            model.addAttribute("hostId", chatRoom.getHostId());
            model.addAttribute("guestId", chatRoom.getGuestId());
            model.addAttribute("roomId", chatRoom.getId());
            model.addAttribute("chatRoom", chatRoom);
            model.addAttribute("roomUUID", chatRoom.getRoomUUID());
            model.addAttribute("roomName", chatRoom.getPetsitter().getPetTitle());

            return "/chat/roomdetail";
        } else {
            return "redirect:/chat/room";
        }
    }
}