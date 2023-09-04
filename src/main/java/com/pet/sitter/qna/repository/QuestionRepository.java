package com.pet.sitter.qna.repository;

import com.pet.sitter.common.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    //Question게시판 전체목록 조회
    List<Question> findAllByOrderByQnaNoDesc();
}
