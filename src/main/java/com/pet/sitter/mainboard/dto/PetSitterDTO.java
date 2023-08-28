package com.pet.sitter.mainboard.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PetSitterDTO {
    private Integer sitter_no;
    private String pet_title;
    private String pet_content;
    private String category;
    private Integer pet_viewCnt;
    private Integer like;
    private Integer file_no;
    private String file_name;
    private String type;
    private Integer price;
    private Integer pet_address_no;
    private String id;
    private String pet_category;
    private LocalDateTime pet_date;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private String week;
}
