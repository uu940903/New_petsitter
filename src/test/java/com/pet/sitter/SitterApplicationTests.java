package com.pet.sitter;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.mainboard.repository.PetsitterFileRepository;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SitterApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PetsitterRepository petsitterRepository;
	Petsitter petsitter = new Petsitter();

	@Autowired
	private PetsitterFileRepository petsitterFileRepository;

	@Test
	void contextLoads() {
	}

/*	@Test
	void testDB(){
		Member member = new Member();
		member.setId("xiuxiu");
		member.setPw("900326");
		member.setNickname("슈슈");
		member.setName("김민석");
		member.setPhone("010-3349-0362");
		member.setBirth("1990-03-26");
		member.setE_mail("xiuxiu@petSitter.com");
		member.setAddress("서울시 강서구 방화동로 12가길 22-1");
		member.setIsshow("Y");
		memberRepository.save(member);
	}*/

/*	@Test
	void selectDB(){
		Long saveId = petsitterRepository.save(petsitter).getSitterNo(); //현재 입력한 글번호를 받아오는 코드
		Petsitter board = petsitterRepository.findById(saveId).get();
		System.out.println(saveId);
		System.out.println(board);
	}*/

}
