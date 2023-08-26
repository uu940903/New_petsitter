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
public class Petsitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sitter_no;

    @Column
    @NotNull
    private String pet_title;

    @Column
    @NotNull
    private String pet_content;

    @Column
    @NotNull
    private String category;

    @Column
    private LocalDateTime pet_date;

    @Column
    @NotNull
    private Integer pet_view_cnt;

    @Column
    @NotNull
    private Integer pet_like;

    @Column
    private Integer price;

    @Column
    @NotNull
    private String pet_category;

    @Column
    private LocalDateTime start_time;

    @Column
    private LocalDateTime end_time;

    @ManyToOne
    @JoinColumn(name="id", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="pet_address_no", referencedColumnName = "pet_address_no")
    private AreaSearch areaSearch;

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private List<PetsitterFile> petsitterFileList;

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private List<Week> weekList;

    @OneToOne(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private Matching matching;

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoomList;

}
