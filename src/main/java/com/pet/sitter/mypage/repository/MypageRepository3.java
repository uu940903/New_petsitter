package com.pet.sitter.mypage.repository;

import com.pet.sitter.common.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MypageRepository3 extends JpaRepository<Question, Long> {

    //내가쓴글가져오기
    Page<Question> findByMemberMemberId(String name, Pageable pageable);
}
