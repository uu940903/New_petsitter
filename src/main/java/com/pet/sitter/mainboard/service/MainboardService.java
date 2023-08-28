package com.pet.sitter.mainboard.service;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.repository.MainboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainboardService {

    private final MainboardRepository mainboardRepository;
    //글작성
    public Petsitter write (PetSitterDTO petsitterDTO) {
        Petsitter petsitter = new Petsitter();
        petsitter.setPet_title(petsitterDTO.getPet_title());
        petsitter.setPet_content(petsitterDTO.getPet_content());
        mainboardRepository.save(petsitter);
        return petsitter;
    }
}
