package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer message_no;

    @Column(columnDefinition = "text")
    @NotNull
    private String message;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @NotNull
    private LocalDateTime createdate_m;

    @ManyToOne
    @JoinColumn (name = "chat_room_no", referencedColumnName = "chat_room_no")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn (name="id", referencedColumnName = "id")
    private Member member;

}
