package com.pet.sitter.common.entity;

import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Petsitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sitterNo;

    @Column
    @NotNull
    private String petTitle;

    @Column
    @NotNull
    private String petContent;

    @Column
    @NotNull
    private String category;

    @Column
    private LocalDateTime petRegdate;

    @Column
    private Integer petViewCnt;

    @Column
    private Integer likeCnt;

    @Column
    private Integer price;

    @Column
    @NotNull
    private String petCategory;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String petAddress;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private List<PetsitterFile> petsitterFileList;

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Week> weekList;

    @OneToOne(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private Matching matching;

    @OneToMany(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoomList;

    @Builder
    public Petsitter(Long sitterNo, String petTitle, String petContent, String category, LocalDateTime petRegdate, Integer petViewCnt, Integer likeCnt, Integer price, String petCategory, LocalDateTime startTime, LocalDateTime endTime, Member member, String petAddress) {
        this.sitterNo = sitterNo;
        this.petTitle = petTitle;
        this.petContent = petContent;
        this.category = category;
        this.petRegdate = petRegdate;
        this.petViewCnt = petViewCnt;
        this.likeCnt = likeCnt;
        this.price = price;
        this.petCategory = petCategory;
        this.startTime = startTime;
        this.endTime = endTime;
        this.member = member;
        this.petAddress = petAddress;
    }

    public boolean isPetsitterFileListEmpty () {
        return petsitterFileList == null || petsitterFileList.isEmpty();
    }


   /*
    @PostLoad
    public void calculateHour() {
        this.startTimeHour = this.startTime.getHour();
        this.endTimeHour = this.endTime.getHour();
    }
    */

}
