package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainboardFileRepository extends JpaRepository<PetsitterFile, Integer> {
}
