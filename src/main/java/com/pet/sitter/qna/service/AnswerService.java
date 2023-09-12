package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.Answer;
import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.qna.dto.AnswerDTO;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.AnswerRepository;
import com.pet.sitter.qna.repository.QuestionRepository;
import com.pet.sitter.qna.validation.AnswerForm;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void addAnswer(QuestionDTO questionDTO, AnswerForm answerForm, Member member) {
        Question question = Question.builder()
                .qnaNo(questionDTO.getQnaNo())
                .build();

        Answer answer = Answer.builder()
                .id(answerForm.getId())
                .content(answerForm.getContent())
                .createdDate(LocalDateTime.now())
                .question(question)
                .member(member)
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
                .createdDate(LocalDateTime.now())
                .member(answer.getMember())
                .build();
        return answerDTO;
    }

  //댓글 수정 처리
    @Transactional
    public void modify(Long id, AnswerDTO answerDTO,AnswerForm answerForm) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        Answer answer = answerOptional.get();
        if (answerOptional.isPresent()) {
            answer.setId(answerForm.getId());
            answer.setContent(answerForm.getContent());
            answer.setModifiedDate(LocalDateTime.now());
            answer.setQuestion(answerDTO.getQuestion());
            answer.setMember(answerDTO.getMember());
            answerRepository.save(answer);
        }
    }

    //댓글 삭제
    public void answerDelete(Long id) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        if(answerOptional.isPresent()){
            Answer answer = answerOptional.get();
            answerRepository.delete(answer);
        }

    }

}

