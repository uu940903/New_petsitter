package com.pet.sitter.chat.service;

import com.pet.sitter.chat.controller.ChatMessageController;
import com.pet.sitter.chat.dto.ChatRoomDTO;
import com.pet.sitter.chat.repository.ChatRoomRepository;
import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatRoomService {

    @Autowired
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    ChatMessageController chatMessageController;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoomDTO createChatRoom(Long id, String hostId, String guestId) {
        System.out.println("PetSitterId(Service): " + id);
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setPetsitter(petsitterRepository.findBySitterNo(id));
        System.out.println(chatRoom.getPetsitter().getPetTitle());
        chatRoom.setRoomUUID(UUID.randomUUID().toString());
        System.out.println(chatRoom.getRoomUUID());
        chatRoom.setCreateDate(LocalDateTime.now());
        System.out.println(chatRoom.getCreateDate());
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        System.out.println(chatRoom.getId() + "번 채팅방 저장 성공");

        System.out.println("HostId(RoomService): " + memberRepository.findMemberByMemberId(hostId).getId());
        System.out.println("GuestId(RoomService): " + memberRepository.findMemberByMemberId(guestId).getId());

        chatMessageController.enterMessage(chatRoom.getId(), hostId);
        chatMessageController.enterMessage(chatRoom.getId(), guestId);

        return convertToChatRoomDTO(savedChatRoom);
    }

    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    public ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findChatRoomByRoomUUID(roomUUID);
    }

    private ChatRoomDTO convertToChatRoomDTO(ChatRoom chatRoom) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setId(chatRoom.getId());
        chatRoomDTO.setRoomUUID(chatRoom.getRoomUUID());
        chatRoomDTO.setCreateDate(chatRoom.getCreateDate());
        return chatRoomDTO;
    }
}
