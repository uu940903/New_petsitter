package com.pet.sitter.mainboard.service;

import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.mainboard.repository.MainboardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MainboardFileService {

    //yml 파일 설정에서 경로 설정한거 불러와주는 것 같다.
    //@Value("${file.dir}")
    //private String fileDir;
    private final MainboardFileRepository mainboardFileRepository;

    public Integer saveFile (MultipartFile files) {
        if (files.isEmpty()) {
            return null;
        }

        //파일 원본명 추출
        String origName = files.getOriginalFilename();

        //파일 UUID 생성
        String uuid = UUID.randomUUID().toString();

        //확장자 추출
        String extension = origName.substring(origName.lastIndexOf("."));

        //UUID와 확장자 결합
        String savedName = uuid + extension;

        //파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedNAme;

        // 파일 엔티티 생성
        PetsitterFile file = PetsitterFile.builder
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

    }
}
