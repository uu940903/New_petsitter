package com.pet.sitter.qna.service;

import com.pet.sitter.common.entity.*;
import com.pet.sitter.qna.dto.QuestionDTO;
import com.pet.sitter.qna.repository.QuestionFileRepository;
import com.pet.sitter.qna.repository.QuestionRepository;
import com.pet.sitter.qna.validation.QuestionForm;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void savePost(QuestionDTO questionDTO, QuestionForm questionForm, MultipartFile[] file, Member member) throws IOException {

        //질문게시판 엔티티생성
        Question question = Question.builder()
                .qnaNo(questionForm.getQnaNo())
                .qnaTitle(questionForm.getTitle())
                .qnaContent(questionForm.getContent())
                .qnaDate(questionForm.getQnaDate())
                .qnaPw(questionForm.getPassword())
                .questionList(questionForm.getQuestionList())
                .qnaViewCnt(0)
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

            List<Answer> answerList = new ArrayList<>();
            for(Answer answer : question.getAnswerList()){
                answerList.add(answer);
            }

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
                    .member(question.getMember())
                    .questionList(fileList)
                    .answerList(answerList)
                    .build();
            return questionDTO;
        }
        return null;
    }

    //Question게시판 수정
    @Transactional
    public void update(Long qnaNo, QuestionDTO questionDTO, MultipartFile[] newImageFiles) throws IOException {
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);

        if(questionOptional.isPresent()){
            Question question = questionOptional.get();
            question.setQnaTitle(questionDTO.getQnaTitle());
            question.setQnaContent(questionDTO.getQnaContent());

            // 기존 파일 삭제
            List<QuestionFile> filesToDelete = question.getQuestionList();
            for (QuestionFile delete : filesToDelete) {
                String filePath = delete.getQSavedPath();
                File fileToDelete = new File(filePath);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
            }
            // 기존 파일 정보 삭제
            question.getQuestionList().clear();

            // 새로운 파일 업로드 및 정보 저장
            if (newImageFiles != null && newImageFiles.length > 0) {
                String path = "C:/uploadfile/question_img/";

                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                List<QuestionFile> newQuestionFiles = new ArrayList<>(); // 새로운 파일 정보를 저장할 리스트

                for (MultipartFile qnaFile : newImageFiles) {
                    String originalFilename = qnaFile.getOriginalFilename();
                    // null 또는 빈 문자열인 경우 처리를 하지 않도록 조건을 추가
                    if (originalFilename != null && !originalFilename.isEmpty()) {
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                        String newFileName = UUID.randomUUID().toString() + fileExtension;
                        String noSavedPath = path + newFileName;
                        File saveFile = new File(path, newFileName);
                        qnaFile.transferTo(saveFile);

                        QuestionFile questionFile = QuestionFile.builder()
                                .QOrgNm(originalFilename)
                                .QSavedNm(newFileName)
                                .QSavedPath(noSavedPath)
                                .question(question) // Question 엔터티와 연관 설정
                                .build();

                        newQuestionFiles.add(questionFile); // 새로운 파일 정보를 리스트에 추가
                    }
                }
                question.getQuestionList().addAll(newQuestionFiles);
            }
            // 수정된 질문을 저장
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

    //비밀번호 확인
    @Transactional
    public String checkPassword(Long qnaNo, String inputPassword) {
        Optional<Question> questionOptional = questionRepository.findById(qnaNo);

        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            String storedPassword = question.getQnaPw(); // DB에 저장된 비밀번호

            if (storedPassword.equals(inputPassword)) {
                return "success"; // 비밀번호 일치
            } else {
                return "failure"; // 비밀번호 불일치
            }
        }

        return "notfound"; // 해당 글이 없음
    }

}



