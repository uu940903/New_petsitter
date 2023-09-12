package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetsitterFileRepository extends JpaRepository<PetsitterFile, Long> {

    List<PetsitterFile> findAllByPetsitter(Petsitter petsitter);

    List<PetsitterFile> findByPetsitter(Petsitter petsitter);

    /*List<MultipartFile> findAllBy (PetsitterFile petsitterFile);*/
}
