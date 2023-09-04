package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String chatRoomNo;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @NotNull
    private LocalDateTime createdate;

    @ManyToOne
    @JoinColumn(name="sitterNo", referencedColumnName = "sitterNo")
    private Petsitter petsitter;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessages> chatMessagesList;

    @Builder
    public ChatRoom(String chatRoomNo, LocalDateTime createdate, Petsitter petsitter, Member member, List<ChatMessages> chatMessagesList) {
        this.chatRoomNo = chatRoomNo;
        this.createdate = createdate;
        this.petsitter = petsitter;
        this.member = member;
        this.chatMessagesList = chatMessagesList;
    }
}
