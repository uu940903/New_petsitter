package com.pet.sitter.chat.dto;

import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDTO {

    private Long id;
    private String roomUUID;
    private String name;
    private LocalDateTime createDate;
    private PetSitterDTO petSitterDTO;
    private String hostId;
    private String guestId;

    public ChatRoomDTO(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomUUID = chatRoom.getRoomUUID();
        this.name = chatRoom.getName();
        this.createDate = chatRoom.getCreateDate();
        this.hostId = chatRoom.getHostId();
        this.guestId = chatRoom.getGuestId();
    }

    public ChatRoomDTO(ChatRoom chatRoom, PetSitterDTO petSitterDTO) {
        this.id = chatRoom.getId();
        this.roomUUID = chatRoom.getRoomUUID();
        this.name = chatRoom.getName();
        this.createDate = chatRoom.getCreateDate();
        this.petSitterDTO = petSitterDTO;
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .id(this.id)
                .roomUUID(this.roomUUID)
                .name(this.name)
                .createDate(this.createDate)
                .hostId(this.hostId)
                .guestId(this.guestId)
                .build();
    }
}
