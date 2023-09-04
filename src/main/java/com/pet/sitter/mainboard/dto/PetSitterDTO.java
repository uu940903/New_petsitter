package com.pet.sitter.mainboard.dto;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PetSitterDTO {
    private Long sitterNo;
    private String petTitle;
    private String petContent;
    private String category;
    private Integer petViewCnt;
    private Integer LikeCnt;
    private List<PetSitterFileDTO> fileDTOList;
    private Integer price;
    private Member member;
    private String petCategory;
    private LocalDateTime petRegdate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<WeekDTO> weekDTOList;


    @Builder
    public PetSitterDTO(Petsitter petsitter) {
        this.sitterNo = petsitter.getSitterNo();
        this.petTitle = petsitter.getPetTitle();
        this.petContent = petsitter.getPetContent();
        this.category = petsitter.getCategory();
        this.petViewCnt = petsitter.getPetViewCnt();
        this.LikeCnt = petsitter.getLikeCnt();
        this.price = petsitter.getPrice();
        this.petCategory = petsitter.getPetCategory();
        this.petRegdate = petsitter.getPetRegdate();
        this.startTime = petsitter.getStartTime();
        this.endTime = petsitter.getEndTime();
    }



    public Petsitter toEntity(){
        return Petsitter.builder()
                .sitterNo(this.sitterNo)
                .petTitle(this.petTitle)
                .petContent(this.petContent)
                .category(this.category)
                .petRegdate(this.petRegdate)
                .petViewCnt(this.petViewCnt)
                .LikeCnt(this.LikeCnt)
                .price(this.price)
                .petCategory(this.petCategory)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }

}