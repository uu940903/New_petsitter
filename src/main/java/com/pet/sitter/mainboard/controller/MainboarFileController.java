package com.pet.sitter.mainboard.controller;

import com.pet.sitter.mainboard.dto.PetsitterFileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainboarFileController {

    private final PetsitterFileService petsitterFileService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(
            @RequestPart(value="image", required=false) List<MultipartFile> files,
            @RequestPart(value = "petsitterFileDTO") PetsitterFileDTO petsitterFileDTO) throws Exception {

        return petsitterFileService.create(petsitterFileDTO, files);
    }
}
