package com.pet.sitter.mainboard.repository;

import com.pet.sitter.common.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PetsitterRepository extends JpaRepository<Petsitter, Integer> {

 //   List<Petsitter> findAllByOrderBySitteNoDesc();
}
