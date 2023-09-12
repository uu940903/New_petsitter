package com.pet.sitter.notice.repository;

import com.pet.sitter.common.entity.NoticeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {

    Optional<NoticeFile> findByNoFile(Long noFile);
}
