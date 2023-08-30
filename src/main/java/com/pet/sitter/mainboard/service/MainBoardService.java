package com.pet.sitter.mainboard.service;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.mainboard.dto.AreaSearchDTO;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainBoardService {

    @Autowired
    private PetsitterRepository petsitterRepository;
/*

    public List<Petsitter> getList(){
        List<Petsitter> petsitterList = petsitterRepository.findAll();
        return petsitterList;
    }

*/



    public List<PetSitterDTO> getList() {
        List<Petsitter> petsitterList = petsitterRepository.findAll();
        List<PetSitterDTO> petSitterDTOList = new ArrayList<>();
        List<PetSitterFileDTO> petSitterFileDTOList = new ArrayList<>();
        List<WeekDTO> weekList = new ArrayList<>();
        for(int i = 0; i<petsitterList.size(); i++){
            Petsitter petsitter = petsitterList.get(i);
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();

/*            petSitterDTO.setAreaSearchDTO(AreaSearchDTO.builder().areaSearch(petsitterList.get(i).getAreaSearch()).build());
            petSitterFileDTOList.add(i, PetSitterFileDTO.builder().petsitterFile(petsitterList.get(i).getPetsitterFileList().get(i)).build());
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            weekList.add(i, WeekDTO.builder().week(petsitterList.get(i).getWeekList().get(i)).build());
            petSitterDTO.setWeekDTOList(weekList);
            petSitterDTOList.add(i, petSitterDTO);*/


            System.out.println(petSitterDTO);
        }
        return petSitterDTOList;
    }

}
