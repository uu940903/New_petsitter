package com.pet.sitter.mainboard.dto;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.member.dto.MemberDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Integer likeCnt;
    private List<PetSitterFileDTO> fileDTOList;
    private Integer price;
    private String petAddress;
    private MemberDTO member;
    private String petCategory;
    private LocalDateTime petRegdate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<WeekDTO> weekDTOList;

    private List<MultipartFile> boardFile;
    private List<String> originFileName;
    private List<String> newFileName;
    private List<String> type;
    private List<String> filePath;
    private List<Integer> fileSize;
    private int fileAttached;

    @Builder
    public PetSitterDTO(Petsitter petsitter) {
        this.sitterNo = petsitter.getSitterNo();
        this.petTitle = petsitter.getPetTitle();
        this.petContent = petsitter.getPetContent();
        this.category = petsitter.getCategory();
        this.petViewCnt = petsitter.getPetViewCnt();
        this.likeCnt = petsitter.getLikeCnt();
        this.price = petsitter.getPrice();
        this.petAddress = petsitter.getPetAddress();
        this.petCategory = petsitter.getPetCategory();
        this.petRegdate = petsitter.getPetRegdate();
        this.startTime = petsitter.getStartTime();
        this.endTime = petsitter.getEndTime();

        if (!petsitter.isPetsitterFileListEmpty()) {
            List<String> originFileNameList = new ArrayList<>();
            List<String> newFileNameList = new ArrayList<>();
            List<String> typeList = new ArrayList<>();
            List<String> filePathList = new ArrayList<>();
            List<Integer> fileSizeList = new ArrayList<>();

            for (PetsitterFile petsitterFile : petsitter.getPetsitterFileList()) {
                originFileNameList.add(petsitterFile.getOriginFileName());
                newFileNameList.add(petsitterFile.getNewFileName());
                typeList.add(petsitterFile.getType());
                filePathList.add(petsitterFile.getFilePath());
                fileSizeList.add(petsitterFile.getFileSize());
            }
            this.originFileName = originFileNameList;
            this.newFileName = newFileNameList;
            this.type = typeList;
            this.filePath = filePathList;
            this.fileSize = fileSizeList;
        }
    }

    public Petsitter toEntity() {
        return Petsitter.builder()
                .sitterNo(this.sitterNo)
                .petTitle(this.petTitle)
                .petContent(this.petContent)
                .category(this.category)
                .petRegdate(this.petRegdate)
                .petViewCnt(this.petViewCnt)
                .likeCnt(this.likeCnt)
                .price(this.price)
                .petCategory(this.petCategory)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .petAddress(this.petAddress)
                .build();
    }

    public Petsitter dtoToEntity(PetSitterDTO petSitterDTO) {
        Petsitter petsitter = new Petsitter();
        petsitter.setPetsitterFileList(petSitterDTO.toEntity().getPetsitterFileList());
        petsitter.setPetTitle(petSitterDTO.getPetTitle());
        petsitter.setCategory(petSitterDTO.getCategory());
        petsitter.setPetCategory(petSitterDTO.getPetCategory());
        petsitter.setPrice(petSitterDTO.price);
        petsitter.setWeekList(petSitterDTO.toEntity().getWeekList());
        petsitter.setPetAddress(petSitterDTO.getPetAddress());
        petsitter.setStartTime(petSitterDTO.getStartTime());
        petsitter.setEndTime(petSitterDTO.getEndTime());
        petsitter.setPetContent(petSitterDTO.getPetContent());
        return petsitter;
    }
}
