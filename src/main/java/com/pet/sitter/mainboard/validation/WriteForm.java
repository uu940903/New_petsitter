package com.pet.sitter.mainboard.validation;

import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class WriteForm {

    private List<PetSitterFileDTO> fileDTOList;
    private List<MultipartFile> boardFile;

    @NotBlank (message = "제목은 필수 입력 사항입니다.")
    private String petTitle;

    @NotBlank (message = "펫종류는 필수 입력 사항입니다.")
    private String petCategory;

    @NotBlank (message = "구인구직은 필수 입력 사항입니다.")
    private String category;

    private Integer price;

    private String day;

    private String petAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotBlank (message = "내용은 필수 입력 사항입니다.")
    private String petContent;

}
