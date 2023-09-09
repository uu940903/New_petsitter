package com.pet.sitter;

import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.common.entity.Week;
import com.pet.sitter.mainboard.repository.PetsitterFileRepository;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.mainboard.repository.PetsitterSpec;
import com.pet.sitter.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
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

	@Test
	void selectPetsitter() {
		String category = "search";
		String petCategory = "all";
		String petAddress = "서울";
		List<String> dayList = new ArrayList<>();
		dayList.add("월");
		dayList.add("화");
		dayList.add("수");
		dayList.add("목");
		dayList.add("금");
		int startTimeHour = 0;
		int endTimeHour = 0;
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("petViewCnt"));
		sorts.add(Sort.Order.desc("petRegdate"));
		Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
			Page<Petsitter> petsitterPage = petsitterRepository.findAll(PetsitterSpec.searchWith(category, petCategory, petAddress, dayList, startTimeHour, endTimeHour), pageable);
			System.out.println("전체 페이지 수 : " + petsitterPage.getTotalPages());
			System.out.println("전체 게시글 수 : " + petsitterPage.getSize());
			for (Petsitter petsitter : petsitterPage.getContent()) {
				System.out.println(petsitter.getPetAddress());
				System.out.println(petsitter.getCategory());
				System.out.println(petsitter.getPetCategory());
				for(Week week : petsitter.getWeekList()) {
					System.out.println(week.getDay());
				}
			}
	}
}
