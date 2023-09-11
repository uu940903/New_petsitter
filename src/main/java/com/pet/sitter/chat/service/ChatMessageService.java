package com.pet.sitter.chat.service;

import com.pet.sitter.chat.dto.ChatMessageDTO;
import com.pet.sitter.chat.dto.ChatRoomDTO;
import com.pet.sitter.chat.repository.ChatMessageRepository;
import com.pet.sitter.chat.repository.ChatRoomRepository;
import com.pet.sitter.chat.repository.MatchingRepository;
import com.pet.sitter.common.entity.*;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final PetsitterRepository petsitterRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final MatchingRepository matchingRepository;

    public void enterMessage(Long chatRoomId, String memberId) {

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        Member member = memberRepository.findMemberByMemberId(memberId);
        PetSitterDTO petSitterDTO = new PetSitterDTO(petsitterRepository.findBySitterNo(chatRoom.getPetsitter().getSitterNo()));

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(chatRoom, petSitterDTO);
        MemberDTO memberDTO = new MemberDTO(member);
        ChatMessageDTO message = new ChatMessageDTO();

        message.setChatRoom(chatRoomDTO);
        message.setSender(memberDTO);
        message.setContent(memberDTO.getNickname() + "님이 입장하셨습니다.");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getContent());
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(member);
        chatMessageRepository.save(chatMessage);

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

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getRoomUUID(), message);
    }

    public List<ChatMessageDTO> getMessageListByRoomId(Long roomId) {
        List<ChatMessage> messageList = chatMessageRepository.findChatMessagesByChatRoom_Id(roomId);
        return messageList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        ChatMessageDTO messageDTO = new ChatMessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setSender(new MemberDTO(message.getSender()));
        messageDTO.setSendTime(message.getSendTime());

        return messageDTO;
    }

    public List<ChatMessageDTO> getPreviousMessages(String roomUUID) {
        return chatMessageRepository.findChatMessagesByChatRoomRoomUUID(roomUUID)
                .stream()
                .map(entity -> new ChatMessageDTO(
                        entity.getId(),
                        new ChatRoomDTO(entity.getChatRoom()),
                        new MemberDTO(entity.getSender()),
                        entity.getContent(),
                        entity.getSendTime()
                ))
                .collect(Collectors.toList());
    }

    public void matching(Long roomId, String hostId, String guestId) {
        Matching matching = new Matching();
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId);
        Optional<Member> member = memberRepository.findBymemberId(hostId);
        if (member.isEmpty()) {
            throw new DataNotFoundException("회원이 없습니다.");
        }
        Member hostMember = member.get();
        Optional<Member> member2 = memberRepository.findBymemberId(guestId);
        if (member2.isEmpty()) {
            throw new DataNotFoundException("회원이 없습니다.");
        }
        Member guestMember = member2.get();

        Petsitter petsitter = chatRoom.getPetsitter();
        matching.setPetsitter(petsitter);
        matching.setCreatdateMatching(LocalDateTime.now());
        matching.setMember(hostMember);
        matching.setMember2(guestMember);
        matching.setChatRoomNo(roomId);
        matchingRepository.save(matching);
    }

    public void delMatching(Long chatRoomNo) {
        Matching matching = matchingRepository.findMatchingByChatRoomNo(chatRoomNo);
        matchingRepository.delete(matching);
    }
}