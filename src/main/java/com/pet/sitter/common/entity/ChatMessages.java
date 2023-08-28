package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageNo;

    @Column(columnDefinition = "text")
    @NotNull
    private String message;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @NotNull
    private LocalDateTime createdateMsg;

    @ManyToOne
    @JoinColumn (name = "chatRoomNo", referencedColumnName = "chatRoomNo")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn (name="id", referencedColumnName = "id")
    private Member member;

    @Builder
    public ChatMessages(Long messageNo, String message, LocalDateTime createdateMsg) {
        this.messageNo = messageNo;
        this.message = message;
        this.createdateMsg = createdateMsg;
    }
}
