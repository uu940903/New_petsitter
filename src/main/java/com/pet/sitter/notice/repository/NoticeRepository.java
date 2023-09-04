package com.pet.sitter.notice.repository;

import com.pet.sitter.common.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    //공지게시판 목록
    List<Notice> findAllByOrderByNoNoDesc();
    
    //공지게시판 글수정
    Optional<Notice> findByNoNo(Long noNo);



    Notice findTopByOrderByNoNoDesc();
}
