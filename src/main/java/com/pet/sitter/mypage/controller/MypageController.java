package com.pet.sitter.mypage.controller;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.mypage.dto.MatchingDTO;
import com.pet.sitter.mypage.service.MypageService;
import com.pet.sitter.mypage.validation.ModifyForm;
import com.pet.sitter.mypage.validation.PassModifyForm;
import com.pet.sitter.qna.dto.QuestionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

//#authentication.getPrincipal().getMemberId
/*    @GetMapping("/info/{nada}")
    public String mypageinfo(@PathVariable("nada") String memberId, Model model){
        MemberDTO memberDTO = mypageService.getMember(memberId);
        model.addAttribute("memberDTO",memberDTO);
        return "myInfo";
    }*/

    //매칭가져오기
    @GetMapping("/machingResult")
    public String machingResult(@RequestParam(value="page",defaultValue="0") int page, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        System.out.println(memberId);
        MemberDTO memberDTO = mypageService.getMember(memberId);//내정보 dto로가져오기
        long id = memberDTO.getId();//내정보의 pk값 id에저장


        Page<PetSitterDTO> petSitterDTOPage= this.mypageService.getMatchingArticleList(id,page);//매칭된 글가져오기
        Page<MatchingDTO> matchingDTO= this.mypageService.getMatchingList(id,page);//매칭내역가져오기

        model.addAttribute("petsitterPage",petSitterDTOPage);
        model.addAttribute("matchingPage",matchingDTO);
        return "mypage/myMatchingList";
    }






    /*//jointest
    @GetMapping("/machingResult")
    public String machingResult(@RequestParam(value="page",defaultValue="0") int page, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        System.out.println(memberId);
        MemberDTO memberDTO = mypageService.getMember(memberId);//내정보 dto로가져오기
        long id = memberDTO.getId();//내정보의 pk값 id에저장


        Page<PetSitterDTO> petsitterPage= this.mypageService.getMatchingArticleList(id,page);

        model.addAttribute("petsitterPage",petsitterPage);
        return "mypage/myMatchingList";
    }*/




    //내정보 보기
    @GetMapping("/info")
    public String mypageinfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        MemberDTO memberDTO = mypageService.getMember(memberId);
        model.addAttribute("memberDTO", memberDTO);
        return "myInfo";
    }
    /*@GetMapping("/myArticleList/{xiuxiu}")
    public String myArticle(@PathVariable("xiuxiu") String id,
                            @RequestParam(value="page",defaultValue="0") int page, Model model){
        Page<PetSitterDTO> petsitterPage= this.mypageService.getMyArticleList(id,page);

        model.addAttribute("petsitterPage",petsitterPage);
        return "mypage/myArticleList";
    }*/

    //나의 작성글 보기
    @GetMapping("/myArticleList")
    public String myArticle(@RequestParam(value="page",defaultValue="0") int page, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        System.out.println(memberId);

        Page<PetSitterDTO> petsitterPage= this.mypageService.getMyArticleList(memberId,page);

        model.addAttribute("petsitterPage",petsitterPage);
        return "mypage/myArticleList";
    }


    //나의 QnA게시글보기
    @GetMapping("/myQnAList")
    public String myQnA(@RequestParam(value="page",defaultValue="0") int page, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        System.out.println(memberId);
        Page<QuestionDTO> questionPage= this.mypageService.getMyQnAList(memberId,page);

        model.addAttribute("questionPage",questionPage);
        return "mypage/myQnAList";
    }



    //


    /*//나의 매칭내역보기
    @GetMapping("/myMatchingList")
    public String myMatching(@RequestParam(value="page",defaultValue="0") int page, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기

        MemberDTO memberDTO = mypageService.getMember(memberId);
        System.out.println("접속중사용자의 id"+memberDTO.getId());
        long id=memberDTO.getId();// 현재 사용자의 member의 id값저장
        Page<PetSitterDTO> matchingPage= this.mypageService.getMyMatchingList(memberId,page);

        model.addAttribute("matchingPage",matchingPage);
        return "mypage/myMatchingList";
    }*/



    //글 상세 보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        //1.파라미터받기
        //2.비즈니스로직수행
        PetSitterDTO petsitter = mypageService.getPetsitter(id);
        //3.Model
        model.addAttribute("petsitter",petsitter);
        //4.View
        return "mypage/question_detail"; //templates폴더하위  question_detail.html
    }
    //비번수정
    @GetMapping("/passmodify")
    public String passmodify(PassModifyForm passModifyForm){
        System.out.println("여기는 Getpassmodify");
        return "mypage/passModify"; //templates폴더하위  question_detail.html
    }

    @PostMapping("/passmodify")
    public String passmodify(@Valid PassModifyForm passModifyForm,
                             BindingResult bindingResult){
        System.out.println("여기는 Postpassmodify");
        if(bindingResult.hasErrors()){ //에러가 존재하면
            System.out.println("bindingResult.hasErrors()에러발생");
            return "mypage/passModify";//templates폴더하위의 signup_form.html문서를 보여줘
        }
        if( !passModifyForm.getPassword1().equals(passModifyForm.getPassword2())){
            bindingResult.rejectValue("password2","passwordInCorrect","비밀번호와 비빌번호확인 일치하지 않습니다.");
            return "mypage/passModify";//templates폴더하위의 signup_form.html문서를 보여줘
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();//접속자의 id받아와서 id 에 저장
        Member member = mypageService.getMemberEntity(id);
        mypageService.passModify(member,passModifyForm.getPassword1());
        //4.View
        return "redirect:/member/login"; //templates폴더하위  question_detail.html
    }

    //이름 주소 폰번 수정
    @GetMapping("/modifyInfo")
    public String modifyInfo(ModifyForm modifyForm){
        System.out.println("여기는 modifyInfo");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        MemberDTO memberDTO = mypageService.getMember(memberId);//해당글 DTO로 가져오기
        modifyForm.setAddress(memberDTO.getAddress());//modifyForm에 DTO의 필드값 넣어주기
        modifyForm.setName(memberDTO.getName());
        modifyForm.setPhone(memberDTO.getPhone());
        modifyForm.setNickname(memberDTO.getNickname());

        return "mypage/modify_Form"; //mypage하위의 modify_Form불러오기
    }


    @PostMapping("/modifyInfo")
    public String modify(@Valid ModifyForm modifyForm,
                         BindingResult bindingResult){
        //1.파라미터받기

        System.out.println("여기는 PostmodifyInfo");
        if(bindingResult.hasErrors()){ //에러가 존재하면
            System.out.println("bindingResult.hasErrors()에러발생");
            return "mypage/modify_Form";//templates폴더하위의 signup_form.html문서를 보여줘
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();//접속자의 id받아와서 id 에 저장
        Member member = mypageService.getMemberEntity(id);// 접속자의 Member 엔티티 가져오기

        //서비스로 접속자의 member랑 modifyForm에서 유효성검사완료된 값들 전달
        mypageService.infoModify(member,modifyForm.getAddress(),modifyForm.getName(),modifyForm.getPhone(),modifyForm.getNickname());

        return "redirect:/mypage/info"; //수정상세페이지로 이동
    }


    /*@PostMapping("/modify/{id}")
    public String modify(@Valid QuestionForm questionForm,BindingResult bindingResult,
                         @PathVariable("id") Integer id,Principal principal){
        //1.파라미터받기
        if(bindingResult.hasErrors()){  //유효성검사시 에러가 발생하면
            return "question_form"; //question_form.html문서로 이동
        }
        //2.비즈니스로직수행
        //로그인한 유저가 글쓴이와 일치해야지만 수정권한을 가지게 된다=>수정처리 진행됨
        Question question = questionService.getQuestion(id); //질문상세
        if ( !question.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        questionService.modify(question,questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%d",id); //수정상세페이지로 이동
    }*/

}
