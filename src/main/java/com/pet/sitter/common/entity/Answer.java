package com.pet.sitter.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Setter
public class Answer  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "createdDate")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modifiedDate")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "qnaNo", referencedColumnName = "qnaNo")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;


    @Builder
    public Answer(Long id,String content,LocalDateTime createdDate, LocalDateTime modifiedDate,Question question,Member member){
        this.id= id;
        this.content = content;
        this.question =question ;
        this.createdDate =createdDate ;
        this.modifiedDate =modifiedDate ;
        this.member =member ;
    }
}