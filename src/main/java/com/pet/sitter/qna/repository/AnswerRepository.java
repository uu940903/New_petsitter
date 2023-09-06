package com.pet.sitter.qna.repository;

import com.pet.sitter.common.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
