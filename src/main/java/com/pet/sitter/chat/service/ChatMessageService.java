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
        PetSitterDTO petSitterDTO = new PetSitterDTO(petsitterRepository.findBySitterNo(chatRoom.getPetsitter().getSitterNo()));

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(chatRoom, petSitterDTO);
        System.out.println("MemberId: " + member.getId());
        MemberDTO sender = new MemberDTO(member);
        ChatMessageDTO message = new ChatMessageDTO();

        message.setType(MessageType.ENTER);
        message.setChatRoom(chatRoomDTO);
        message.setSender(sender);
        message.setContent(sender.getNickname() + "님이 입장하셨습니다.");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getContent());
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(member);
        chatMessageRepository.save(chatMessage);

        System.out.println(message.getContent());
        System.out.println("MemberId: " + message.getSender().getId());

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getId(), message);
    }

    public void sendMessage(ChatMessageDTO message) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getChatRoom().getId());
        Member sender = memberRepository.findByNickname(message.getSender().getNickname());
        MemberDTO member = new MemberDTO(sender);
        System.out.println(member.getMemberId() + "|" + member.getNickname());
        message.setSender(member);


        if (MessageType.ENTER.equals(message.getType())) {
            message.setContent(sender.getNickname() + "님이 입장하셨습니다.");
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getContent());
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getId(), message);
    }
}