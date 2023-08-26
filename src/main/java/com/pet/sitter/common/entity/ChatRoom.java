package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chat_room_no;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @NotNull
    private LocalDateTime createdate_r;

    @ManyToOne
    @JoinColumn(name="sitter_no", referencedColumnName = "sitter_no")
    private Petsitter petsitter;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessages> chatMessagesList;

}
