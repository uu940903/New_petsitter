package com.pet.sitter.mainboard.dto;

import com.pet.sitter.common.entity.AreaSearch;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AreaSearchDTO {
    private Long petAddressNo;
    private String gu;
    private String roadname;
    private String detailAddress;
    private String post;

    @Builder
    public AreaSearchDTO(AreaSearch areaSearch) {
        this.petAddressNo = areaSearch.getPetAddressNo();
        this.gu = areaSearch.getGu();
        this.roadname = areaSearch.getRoadname();
        this.detailAddress = areaSearch.getDetailAddress();
        this.post = areaSearch.getPost();
    }

    public AreaSearch toEntity(){
        return AreaSearch.builder()
                .petAddressNo(this.petAddressNo)
                .gu(this.gu)
                .roadname(this.roadname)
                .detailAddress(this.detailAddress)
                .post(this.post)
                .build();
    }
}
