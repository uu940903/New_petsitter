package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.qna.dto.AnswerDTO;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.AnswerRepository;
import com.pet.sitter.qna.repository.QuestionRepository;
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
    public void addAnswer(QuestionDTO questionDTO, Member member,String content) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setContent(content);
        answerDTO.setCreatedDate(LocalDateTime.now());
        answerDTO.setQuestion(questionDTO.toEntity());
        answerDTO.setMember(member);

        answerRepository.save(answerDTO.toEntity());
    }


    //댓글 수정
    @Transactional
    public Question detail(Long qnaNo) {
        Optional<Question> answer = questionRepository.findById(qnaNo);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new RuntimeException();
        }
    }

    //댓글 수정처리
    public void answerModify(QuestionDTO questionDTO, String comment) {
        questionDTO.setQnaDate(LocalDateTime.now());
        questionRepository.save(questionDTO.toEntity());
    }

    //댓글 삭제
    public void answerDelete(QuestionDTO questionDTO) {
        questionRepository.delete(questionDTO.toEntity());
    }

}
