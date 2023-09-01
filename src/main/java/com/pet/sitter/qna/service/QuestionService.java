package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.Question;
import com.pet.sitter.notice.dto.NoticeDTO;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.QuestionRepository;
import jakarta.persistence.GenerationType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class QuestionService {
    private  final QuestionRepository questionRepository;
    @Autowired
    public QuestionService(QuestionRepository qnARepository) {
        this.questionRepository = qnARepository;
    }


    //Entity --> DTO 변환
    //Question 글 등록

    @Transactional
    public Long savePost(QuestionDTO questionDTO) {
        Question question = new Question();
        question.getMember().getId();
        return questionRepository.save(questionDTO.toEntity()).getQnaNo();
    }



    //Question게시판 목록 전체조회
    @Transactional
    public List<QuestionDTO> questionList(){
        List<Question> questionList = questionRepository.findAllByOrderByQnaNoDesc();
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questionList) {
            QuestionDTO questionDTO =  QuestionDTO.builder()
                    .qnaTitle(question.getQnaTitle())
                    .qnaViewCnt(question.getQnaViewCnt())
                    .build();
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    //Question게시판 수정
    @Transactional
    public void Update(Long qnaNo, QuestionDTO questionDTO, Principal principal){
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);
        if(questionOptional.isPresent()){
            Question question = questionOptional.get();
            question.setQnaNo(questionDTO.getQnaNo());
            question.setQnaTitle(questionDTO.getQnaTitle());
            question.setQnaContent(questionDTO.getQnaContent());
            question.setQnaPw(questionDTO.getQnaPw());
            questionRepository.save(question);
        }
    }

}

