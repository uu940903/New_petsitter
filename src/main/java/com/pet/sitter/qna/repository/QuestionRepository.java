package com.pet.sitter.qna.repository;

import com.pet.sitter.common.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    //Question게시판 전체목록 조회
    List<Question> findAllByOrderByQnaNoDesc();

    //질문게시판 글수정
    Optional<Question> findByQnaNo(Long qnoNo);

    //페이징 기능이 있는 질문 목록 조회
    Page<Question> findAll(Pageable pageable);
}

