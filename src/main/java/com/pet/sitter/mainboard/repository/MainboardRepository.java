package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainboardRepository extends JpaRepository<Petsitter, Integer> {

}
