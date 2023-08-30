package com.pet.sitter.mainboard.dto;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PetSitterFileDTO {

    private List<MultipartFile> boardFile; //html에서 controller로 파일 담는 용도

    private Long fileNo;
    private String originFileName;
    private String newFileName;
    private String filePath;
    private String type;

    @Builder
    public PetSitterFileDTO(PetsitterFile petsitterFile) {
        this.fileNo = petsitterFile.getFileNo();
        this.originFileName = petsitterFile.getOriginFileName();
        this.newFileName = petsitterFile.getNewFileName();
        this.filePath = petsitterFile.getFilePath();
        this.type = petsitterFile.getType();
    }

    public PetsitterFile toEntity(){
        return PetsitterFile.builder()
                .fileNo(this.fileNo)
                .originFileName(this.originFileName)
                .newFileName(this.newFileName)
                .filePath(this.filePath)
                .type(this.type)
                .build();
    }
}
