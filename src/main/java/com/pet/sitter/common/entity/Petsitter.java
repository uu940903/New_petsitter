package com.pet.sitter.common.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    /*@OneToOne(mappedBy = "petsitter", cascade = CascadeType.REMOVE)
    private Matching matching;*/

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

    public boolean isPetsitterFileListEmpty() {
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
