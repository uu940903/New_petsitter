package com.pet.sitter.mainboard.validation;

import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class WriteForm {

    //private List<PetSitterFileDTO> fileDTOList;
    private List<MultipartFile> boardFile;
    private List<String> weekList;

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    private String petTitle;

    @NotBlank(message = "펫종류는 필수 입력 사항입니다.")
    private String petCategory;

    @NotBlank(message = "구인구직은 필수 입력 사항입니다.")
    private String category;

    private Integer price;

    private String day;

    private String petAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String petContent;

    public PetSitterDTO convertToPetSitterDTO() {
        PetSitterDTO dto = new PetSitterDTO();

        dto.setPetTitle(this.petTitle);
        dto.setPetCategory(this.petCategory);
        dto.setCategory(this.category);
        dto.setPrice(this.price);
        dto.setPetAddress(this.petAddress);
        dto.setStartTime(this.startTime);
        dto.setEndTime(this.endTime);
        dto.setPetContent(this.petContent);

        // WeekDTO 리스트로
        List<WeekDTO> weekDTOs = new ArrayList<>();
        for (String week : weekList) {
            WeekDTO weekDTO = new WeekDTO();
            weekDTO.setDay(week);
            weekDTOs.add(weekDTO);
        }
        dto.setWeekDTOList(weekDTOs);

        // boardFile 처리. MultipartFile을 다루는 로직은 서비스 레이어에서 처리하도록 남겨두는 것이 좋습니다.
        // 여기에서는 MultipartFile 리스트만 DTO에 설정합니다.
        dto.setBoardFile(this.boardFile);

        return dto;
    }
}
