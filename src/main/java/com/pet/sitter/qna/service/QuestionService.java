package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.*;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.QuestionFileRepository;
import com.pet.sitter.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionFileRepository questionFileRepository;

    @Autowired
    public QuestionService(QuestionRepository qnARepository, QuestionFileRepository questionFileRepository) {
        this.questionRepository = qnARepository;
        this.questionFileRepository = questionFileRepository;
    }


    //Entity --> DTO 변환
    //Question 글 등록

    @Transactional
    public void savePost(QuestionDTO questionDTO, MultipartFile[] file) throws IOException {
        Member member = new Member();
        member.setId(6L);
        questionDTO.setMember(member);

        //질문게시판 엔티티생성
        Question question = Question.builder()
                .qnaNo(questionDTO.getQnaNo())
                .qnaTitle(questionDTO.getQnaTitle())
                .qnaContent(questionDTO.getQnaContent())
                .qnaDate(questionDTO.getQnaDate())
                .qnaPw(questionDTO.getQnaPw())
                .qnaViewCnt(0)
                .qnaFile(questionDTO.getQnaFile())
                .member(member)
                .build();
        if (file[0].isEmpty()) {
            questionRepository.save(question);
        } else {
            Long boardNo = questionRepository.save(question).getQnaNo();
            Question Question1 = questionRepository.findById(boardNo).get();

            QuestionFile questionFile = new QuestionFile();

            String path = "C:/uploadfile/question_img/";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            for (MultipartFile qFile : file) {
                String originalFilename = qFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExtension;
                String noSavedPath = path + newFileName;
                File saveFile = new File(path, newFileName);
                qFile.transferTo(saveFile);
                //          noFile.transferTo(new File(noSavedPath));

                questionFile = QuestionFile.builder()
                        .QOrgNm(originalFilename)
                        .QSavedNm(newFileName)
                        .QSavedPath(noSavedPath)
                        .QcreateDate(LocalDate.now())
                        .build();

                questionFile.setQuestion(Question1);
                questionFileRepository.save(questionFile);
            }

            questionRepository.save(question);
        }
    }


    //Question게시판 목록 전체조회
    @Transactional
    public Page<Question> questionList(int page){
        List <Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("qnaDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);

    }

    //Question게시판 상세내용
    @Transactional
    public QuestionDTO detail(Long qnaNo){
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);
        if(questionOptional.isPresent()) {
            Question question = questionOptional.get();
            question.increaseViewCount();
            questionRepository.save(question);

            List<QuestionFile> fileList = new ArrayList<>(); // NoticeFile을 담을 리스트 선언
            for (QuestionFile questionFile : question.getQuestionList()) {
                fileList.add(questionFile); // NoticeFile을 리스트에 추가
            }

            QuestionDTO questionDTO = QuestionDTO.builder()
                    .qnaNo(question.getQnaNo())
                    .qnaTitle(question.getQnaTitle())
                    .qnaDate(question.getQnaDate())
                    .qnaContent(question.getQnaContent())
                    .qnaViewCnt(question.getQnaViewCnt())
                    .qnaFile(question.getQnaFile())
                    .member(question.getMember())
                    .QuestionList(fileList)
                    .build();
            return questionDTO;
        }
        return null;
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

