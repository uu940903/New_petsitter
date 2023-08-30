package com.pet.sitter.mainboard.repository;


import com.pet.sitter.common.entity.Week;
import com.pet.sitter.common.entity.WeekId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekRepository extends JpaRepository<Week, WeekId> {
}
