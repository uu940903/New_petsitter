package com.pet.sitter.qna.validation;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.QuestionFile;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QuestionForm {

    @NotEmpty(message = "제목은 필수 입력입니다.")
    @Size(max = 200)
    private String title;

    @NotEmpty(message = "내용은 필수 입력입니다.")
    private String content;

    @NotEmpty(message = "비밀번호로 사용할 숫자4자리를 입력해주세요")
    @Size(max = 4)
    private String password;

    private Long qnaNo;
    private LocalDateTime qnaDate = LocalDateTime.now();
    private Integer qnaViewCnt = 0;
    private Member member;

    private List<MultipartFile> file;
    private List<QuestionFile> questionList;
}
