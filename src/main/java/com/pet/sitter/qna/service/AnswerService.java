package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.Answer;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.qna.dto.AnswerDTO;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.AnswerRepository;
import com.pet.sitter.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(QuestionRepository qnARepository, AnswerRepository answerRepository) {
        this.questionRepository = qnARepository;
        this.answerRepository = answerRepository;
    }

    //Entity --> DTO변환
    //Answer 글 등록
    @Transactional
    public void addAnswer(QuestionDTO questionDTO, AnswerDTO answerDTO) {
        Question question = questionRepository.findById(questionDTO.getQnaNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid qnaNo: " + questionDTO.getQnaNo()));
        Answer answer = Answer.builder()
                .id(answerDTO.getId())
                .content(answerDTO.getContent())
                .question(question)
                .createdDate(answerDTO.getCreatedDate())
                .modifiedDate(answerDTO.getModifiedDate())
                .build();
        answerRepository.save(answer);
    }

    //댓글 리스트
    @Transactional
    public AnswerDTO getDetail(Long id) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        Answer answer = answerOptional.get();
        if (answerOptional.isPresent()) {
            answerRepository.save(answer);
        }

        AnswerDTO answerDTO = AnswerDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .question(answer.getQuestion())
                .createdDate(answer.getCreatedDate())
                .member(answer.getMember())
                .build();
        System.out.println(answerDTO);
        return answerDTO;
    }

    //댓글 수정
    @Transactional
    public void modify(Long id, QuestionDTO questionDTO, AnswerDTO answerDTO) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            answer.setId(answerDTO.getId());
            answer.setContent(answerDTO.getContent());
            answer.setModifiedDate(answerDTO.getModifiedDate());
            answer.setQuestion(answerDTO.getQuestion());
            answer.setMember(answerDTO.getMember());
            answerRepository.save(answer);
        }
    }
}
//
//
//    //댓글 삭제
//    public void answerDelete(QuestionDTO questionDTO) {
//        questionRepository.delete(questionDTO.toEntity());
//    }
//
//}
