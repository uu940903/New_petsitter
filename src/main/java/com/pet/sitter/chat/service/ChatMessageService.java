package com.pet.sitter.chat.service;

import com.pet.sitter.chat.dto.ChatMessageDTO;
import com.pet.sitter.chat.dto.ChatRoomDTO;
import com.pet.sitter.chat.dto.MessageType;
import com.pet.sitter.chat.repository.ChatMessageRepository;
import com.pet.sitter.chat.repository.ChatRoomRepository;
import com.pet.sitter.common.entity.ChatMessage;
import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final PetsitterRepository petsitterRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public void enterMessage(Long chatRoomId, String memberId) {

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        Member member = memberRepository.findMemberByMemberId(memberId);
        System.out.println("testMemberId: " + member.getId());
        System.out.println("********2." + member.getNickname() + "********");
        PetSitterDTO petSitterDTO = new PetSitterDTO(petsitterRepository.findBySitterNo(chatRoom.getPetsitter().getSitterNo()));

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(chatRoom, petSitterDTO);
        MemberDTO memberDTO = new MemberDTO(member);
        System.out.println("testMemberId: " + memberDTO.getId());
        System.out.println("********2." + memberDTO.getNickname() + "********");
        ChatMessageDTO message = new ChatMessageDTO();

        message.setType(MessageType.ENTER);
        message.setChatRoom(chatRoomDTO);
        message.setSender(memberDTO);
        message.setContent(memberDTO.getNickname() + "님이 입장하셨습니다.");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getContent());
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(member);
        chatMessageRepository.save(chatMessage);

        System.out.println(message.getContent());
        System.out.println("MemberId: " + message.getSender().getId());
        System.out.println(member);
        System.out.println(memberDTO);

        messagingTemplate.convertAndSend("/sub/chat/room/enter" + chatRoom.getId(), message);
    }

    public void sendMessage(Long roomId, String memberId, ChatMessageDTO message) {
        System.out.println("----------------MessageService Access----------------");
        System.out.println("chatRoomId: " + roomId);
        System.out.println("chatContent: " + message.getContent());
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId);
        System.out.println("Date: " + chatRoom.getCreateDate());
        Member sender11 = memberRepository.findMemberByMemberId(memberId);
        System.out.println("NickName: " + sender11.getNickname());
        System.out.println("Member11: " + sender11);
        MemberDTO member = new MemberDTO(sender11);

        message.setSender(member);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getContent());
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender11);
        System.out.println("Message Content: " + chatMessage.getContent());
        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getId(), message);
    }
}