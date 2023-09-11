package com.pet.sitter.chat.service;

import com.pet.sitter.chat.controller.ChatMessageController;
import com.pet.sitter.chat.dto.ChatRoomDTO;
import com.pet.sitter.chat.repository.ChatMessageRepository;
import com.pet.sitter.chat.repository.ChatRoomRepository;
import com.pet.sitter.chat.repository.MatchingRepository;
import com.pet.sitter.common.entity.ChatMessage;
import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.common.entity.Matching;
import com.pet.sitter.common.entity.Petsitter;
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
    ChatMessageService chatMessageService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoomDTO createChatRoom(Long id, String hostId, String guestId) {
        Petsitter petsitter = petsitterRepository.findBySitterNo(id);
        if (chatRoomRepository.findChatRoomByPetsitterAndGuestId(petsitter, guestId) == null) {
            ChatRoom chatRoom = new ChatRoom();

            chatRoom.setPetsitter(petsitterRepository.findBySitterNo(id));
            chatRoom.setRoomUUID(UUID.randomUUID().toString());
            chatRoom.setName(chatRoom.getPetsitter().getPetTitle() + " [" +
                    memberRepository.findMemberByMemberId(hostId).getNickname() + " & " +
                    memberRepository.findMemberByMemberId(guestId).getNickname() + "]");
            chatRoom.setCreateDate(LocalDateTime.now());
            chatRoom.setHostId(hostId);
            chatRoom.setGuestId(guestId);

            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

            chatMessageService.enterMessage(chatRoom.getId(), hostId);
            chatMessageService.enterMessage(chatRoom.getId(), guestId);

            return convertToChatRoomDTO(savedChatRoom);
        } else {
            ChatRoom chatRoom = chatRoomRepository.findChatRoomByPetsitterAndGuestId(petsitter, guestId);
            return convertToChatRoomDTO(chatRoom);
        }


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
        System.out.println("chatRoomDTO_ID: " + chatRoomDTO.getId());
        return chatRoomDTO;
    }

    public String getChatRoomUUIDById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not Found"));
        return chatRoom.getRoomUUID();
    }

    public Matching getMatchingByRoomId(Long roomId) {
        System.out.println("before: " + roomId);
        Matching matching = matchingRepository.findMatchingByChatRoomNo(roomId);
        if (matching == null) {
            return null;
        } else {
            System.out.println("after: " + roomId + " | " + matching);
            return matching;
        }
    }
}
