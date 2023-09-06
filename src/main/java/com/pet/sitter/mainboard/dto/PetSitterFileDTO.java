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
    private Long sitterNo;
    private String originFileName; //원본파일이름
    private String newFileName; //서버 저장용 파일 이름
    private String filePath; //
    private String type;


    @Builder
    public PetSitterFileDTO(PetsitterFile petsitterFile) {
        Petsitter petsitter = new Petsitter();
        this.fileNo = petsitterFile.getFileNo();
        this.sitterNo = petsitterFile.getPetsitter().getSitterNo();
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
