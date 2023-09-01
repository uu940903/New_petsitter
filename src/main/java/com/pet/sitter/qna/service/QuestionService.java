package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Question;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class QuestionService {
    private  final QuestionRepository questionRepository;
    private  final MemberRepository memberRepository;
    @Autowired
    public QuestionService(QuestionRepository qnARepository, MemberRepository memberRepository) {
        this.questionRepository = qnARepository;
        this.memberRepository = memberRepository;
    }


    //Entity --> DTO 변환
    //Question 글 등록

    @Transactional
    public Long savePost(QuestionDTO questionDTO) {
        Member member = new Member();
        member.setId(6L);
        questionDTO.setMember(member);
        return questionRepository.save(questionDTO.toEntity()).getQnaNo();
    }



    //Question게시판 목록 전체조회
    @Transactional
    public List<QuestionDTO> questionList(){
        List<Question> questionList = questionRepository.findAllByOrderByQnaNoDesc();
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questionList) {
            QuestionDTO questionDTO =  QuestionDTO.builder()
                    .qnaNo(question.getQnaNo())
                    .qnaTitle(question.getQnaTitle())
                    .qnaViewCnt(question.getQnaViewCnt())
                    .qnaDate(LocalDateTime.now())
                    .member(question.getMember())
                    .build();
            questionDTOList.add(questionDTO);

        }
        return questionDTOList;
    }

    //Question게시판 상세내용
    @Transactional
    public QuestionDTO detail(Long qnaNo){
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);
        Question question = questionOptional.get();

        QuestionDTO questionDTO = QuestionDTO.builder()
                .qnaNo(question.getQnaNo())
                .qnaTitle(question.getQnaTitle())
                .qnaDate(LocalDateTime.now())
                .qnaContent(question.getQnaContent())
                .qnaViewCnt(question.getQnaViewCnt())
                .qnaFile(question.getQnaFile())
                .qnaComment(question.getQnaComment())
                .member(question.getMember())
                .build();
        return questionDTO;

    }

    //Question게시판 수정
    @Transactional
    public void update(Long qnaNo, QuestionDTO questionDTO){
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);

        if(questionOptional.isPresent()){
            Question question = questionOptional.get();
            question.setQnaNo(questionDTO.getQnaNo());
            question.setQnaDate(questionDTO.getQnaDate());
            question.setQnaTitle(questionDTO.getQnaTitle());
            question.setQnaContent(questionDTO.getQnaContent());
            question.setQnaPw(questionDTO.getQnaPw());
            questionRepository.save(question);
        }
    }

    //Question게시판 삭제
    @Transactional
    public void delete(Long qnaNo) {
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            questionRepository.delete(question);
        }
    }

}

