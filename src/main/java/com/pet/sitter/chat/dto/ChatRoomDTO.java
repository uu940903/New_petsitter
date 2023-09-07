package com.pet.sitter.chat.dto;

import com.pet.sitter.common.entity.ChatRoom;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private Long id;
    private String roomUUID;
    private LocalDateTime createDate;
    private PetSitterDTO petSitterDTO;

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .id(this.id)
                .roomUUID(this.roomUUID)
                .createDate(this.createDate)
                .build();
    }
}
