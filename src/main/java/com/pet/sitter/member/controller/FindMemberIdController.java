package com.pet.sitter.member.controller;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller

public class FindMemberIdController {

    private final MemberRepository memberRepository;

    @GetMapping("/memberid")
    public ResponseEntity<String> getEmailByPhoneNumber(@RequestParam String phone) {
        Optional<Member> memberOptional = memberRepository.findByPhone(phone);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String memberId = member.getMemberId();

            return ResponseEntity.ok(memberId);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pw")
    public ResponseEntity<String> getPassword(@RequestParam String memberId) {
        // 여기에서 이메일을 사용하여 필요한 작업을 수행합니다.
        // 예를 들어, 데이터베이스에서 사용자를 찾고 비밀번호를 가져오는 작업을 수행할 수 있습니다.
        // 이 예제에서는 간단하게 이메일을 응답으로 다시 보내는 것으로 대체하겠습니다.

        return ResponseEntity.ok("서버에서 받은 아이디: " + memberId);
    }
}
