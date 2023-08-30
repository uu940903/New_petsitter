package com.pet.sitter.mainboard.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.common.entity.Week;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import com.pet.sitter.mainboard.repository.PetsitterFileRepository;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.mainboard.repository.PetsitterSpec;
import com.pet.sitter.mainboard.repository.WeekRepository;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainBoardService {

    private final Logger logger = LoggerFactory.getLogger(MainBoardService.class);

    private final PetsitterRepository petsitterRepository;
    private final MemberRepository memberRepository;
    private final PetsitterFileRepository petsitterFileRepository;
    private final WeekRepository weekRepository;

    //게시글 목록 조회
    public Page<PetSitterDTO> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("petViewCnt"));
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<Petsitter> petsitterPage = petsitterRepository.findAll(pageable);

        Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            List<PetSitterFileDTO> petSitterFileDTOList = petsitter.getPetsitterFileList().stream()
                    .map(petsitterFile -> PetSitterFileDTO.builder()
                            .petsitterFile(petsitterFile)
                            .build())
                    .collect(Collectors.toList());

            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            return petSitterDTO;
        });

        return petSitterDTOPage;
    }


    /*
    //게시글 목록 조회
    public Page<PetSitterDTO> getList(int page) {
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("petViewCnt"));
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<Petsitter> petsitterPage = petsitterRepository.findAll(pageable);


        int i = 0;
        for (Petsitter petsitter : petsitterPage.getContent()) {
            List<PetSitterDTO> petSitterDTOList = new ArrayList<>();
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            System.out.println("ㅠㅠㅠㅠ" + petsitter.getPetsitterFileList());
            List<PetSitterFileDTO> petSitterFileDTOList = new ArrayList<>();
            for (PetsitterFile petsitterFile : petsitter.getPetsitterFileList()) {
                PetSitterFileDTO petSitterFileDTO = PetSitterFileDTO.builder().petsitterFile(petsitterFile).build();
                System.out.println(petSitterFileDTO);
                petSitterFileDTOList.add(petSitterFileDTO);
            }
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            System.out.println("petSitterDTO : " + petSitterDTO);
            System.out.println("petSitterDTOList 2번 : " + petSitterDTOList);
            petSitterDTOList.add(petSitterDTO);
            System.out.println("petSitterDTOList 3번 : " + petSitterDTOList);
        }

        List<PetSitterDTO> petSitterDTOList = petsitterPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = new PetSitterDTO();
            petSitterDTO.setSitterNo(petsitter.getSitterNo());
            petSitterDTO.setPetTitle(petsitter.getPetTitle());
            petSitterDTO.setPetContent(petsitter.getPetContent());
            petSitterDTO.setCategory(petsitter.getCategory());
            petSitterDTO.setMember(MemberDTO.builder().member(petsitter.getMember()).build());
            petSitterDTO.setPetCategory(petsitter.getPetCategory());
            petSitterDTO.setPetRegdate(petsitter.getPetRegdate());
            petSitterDTO.setPetViewCnt(petsitter.getPetViewCnt());
            petSitterDTO.setPetAddress(petsitter.getPetAddress());
            petSitterDTO.setStartTime(petsitter.getStartTime());
            petSitterDTO.setEndTime(petsitter.getEndTime());
            return petSitterDTO;
        }).getContent();
        System.out.println("petSitterDTOList 4번 : " + petSitterDTOList);
        return new PageImpl<>(petSitterDTOList, pageable, petsitterPage.getTotalElements());
    }
    */

    /*
    //무한 스크롤
    public List<PetSitterDTO> getListInfinite(Long sitterNo, int size){
        Pageable pageable = PageRequest.of(0, size);
        Page<Petsitter> petSitterEntity = petsitterRepository.findAllBySitterNoLessThanOrderByPetRegdateDesc(pageable, sitterNo);
        List<PetSitterDTO> petsitterDTOList = new ArrayList<>();
        for(int i = 0; i<petSitterEntity.toList().size(); i++){
            Petsitter petsitter = petSitterEntity.toList().get(i);
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            petSitterDTO.setAreaSearchDTO(AreaSearchDTO.builder().areaSearch(petsitter.getAreaSearch()).build());
            petSitterDTO.setMember(MemberDTO.builder().member(petsitter.getMember()).build().toEntity());
            petsitterDTOList.add(i, petSitterDTO);
        }
        return petsitterDTOList;
    }
    */


    //게시글 상세 조회
    public PetSitterDTO getDetail(Long no) {
        Petsitter petsitterEntity = petsitterRepository.findBySitterNo(no);
        PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitterEntity).build();
        petSitterDTO.setMember(MemberDTO.builder().member(petsitterEntity.getMember()).build());
        if (!petsitterEntity.getWeekList().isEmpty()) {
            List<WeekDTO> weekDTOList = new ArrayList<>();
            for (int i = 0; i < petsitterEntity.getWeekList().size(); i++) {
                WeekDTO weekDTO = WeekDTO.builder().week(petsitterEntity.getWeekList().get(i)).build();
                weekDTOList.add(weekDTO);
            }
            petSitterDTO.setWeekDTOList(weekDTOList);
        }
        if (!petsitterEntity.getPetsitterFileList().isEmpty()) {
            List<PetSitterFileDTO> petSitterFileDTOList = new ArrayList<>();
            for (int i = 0; i < petsitterEntity.getPetsitterFileList().size(); i++) {
                PetSitterFileDTO petSitterFileDTO = PetSitterFileDTO.builder().petsitterFile(petsitterEntity.getPetsitterFileList().get(i)).build();
                petSitterFileDTOList.add(petSitterFileDTO);
            }
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
        }
        System.out.println("파일 출력 = " + petSitterDTO.getFileDTOList());
        return petSitterDTO;
    }


    //게시글 검색
    public Page<PetSitterDTO> searchList(int page, String category, String petCategory, String petAddress, String day, String timeStr){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("petViewCnt"));
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        List<String> dayList = new ArrayList<>();
        int startTimeHour = 0;
        int endTimeHour = 0;
        switch (day) {
            case "weekday" -> dayList.add(Arrays.toString(new String[]{"월", "화", "수", "목", "금"}));
            case "weekend" -> dayList.add(Arrays.toString(new String[]{"토", "일"}));
            case "allDay" -> dayList = null;
        }

        switch (timeStr) {
            case "dawn" -> {
                startTimeHour = 0;
                endTimeHour = 5;
            }
            case "morning" -> {
                startTimeHour = 6;
                endTimeHour = 11;
            }
            case "afternoon" -> {
                startTimeHour = 12;
                endTimeHour = 17;
            }
            case "midnight" -> {
                startTimeHour = 18;
                endTimeHour = 23;
            }
            case "allTime" -> {
            }
        }

        System.out.println("값 확인 : "+category+", "+petCategory+", "+petAddress+", "+dayList+", "+startTimeHour+", "+endTimeHour);

        Page<Petsitter> petsitterPage = petsitterRepository.findAll(PetsitterSpec.searchWith(category, petCategory, petAddress, dayList, startTimeHour, endTimeHour), pageable);
        Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            List<PetSitterFileDTO> petSitterFileDTOList = petsitter.getPetsitterFileList().stream()
                    .map(petsitterFile -> PetSitterFileDTO.builder()
                            .petsitterFile(petsitterFile)
                            .build())
                    .collect(Collectors.toList());

            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            return petSitterDTO;
        });
        System.out.println();
        return petSitterDTOPage;
    }


    //Petsitter 테이블 insert / 글작성
    public void write(PetSitterDTO petSitterDTO, String id, MultipartFile[] boardFile) throws IOException {
        logger.info("MainBoardService-write()진입");


        //로그인한 유저정보 받아옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<Member> memberOptional = memberRepository.findBymemberId(id);
        Member member = new Member();
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
        }

        //우편 API사용하는데, 우리가 필요로하지 않은 정보들까지 있어서 문자열 자름
        String address = petSitterDTO.getPetAddress();
        int idx = address.indexOf(",");

        String petAddress = address.substring(0, idx);

        petSitterDTO.setPetAddress(petAddress);


        // id 정보 담아주고, MySQL에서 기본값 설정이 JPA에선 안먹어서 다시 설정해줌
        logger.info("Member 정보 : {}", member);
        Petsitter petsitter = petSitterDTO.toEntity();
        petsitter.setMember(member);
        petsitter.setLikeCnt(0);
        petsitter.setPetRegdate(LocalDateTime.now());
        petsitter.setPetViewCnt(0);

        if (petsitter.getPrice() == null) {
            petsitter.setPrice(0);
        }


        Long saveId = petsitterRepository.save(petsitter).getSitterNo(); //현재 입력한 글번호를 받아오는 코드
        Petsitter board = petsitterRepository.findById(saveId).get();


        // Week 엔티티 생성 및 연결
        Week week = new Week();
        week.setPetsitter(petsitter);
        week.setDay("수"); // 예시로 월요일 설정, 필요한 요일로 변경 가능

        List<Week> weekList = new ArrayList<>();
        weekList.add(week);
        petsitter.setWeekList(weekList);

        // Week 엔티티를 저장
        weekRepository.save(week);



        if (boardFile[0].isEmpty()) { //파일이 없을 때
            petsitterRepository.save(petsitter);
        } else { //파일이 있을 때

            // 첨부 파일 있음.
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 내사진.jpg => 839798375892_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
            */

/*            Long saveId = petsitterRepository.save(petsitter).getSitterNo(); //현재 입력한 글번호를 받아오는 코드
            Petsitter board = petsitterRepository.findById(saveId).get();*/

            //Petsitter 구한 번호를 PetsitterFile이랑 연결시켜줘야지


            PetsitterFile petsitterFile = new PetsitterFile();

            String uploadDirectory = "C:/uploadfile/petsitter_img/";
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs(); // 폴더를 생성합니다.
            }

            for (MultipartFile files : boardFile) { //1.
                String originFileName = files.getOriginalFilename(); // 2.
                String newFileName = System.currentTimeMillis() + "_" + originFileName; // 3.
                String filePath = uploadDirectory + newFileName; // 4. C:/petsitter_img/9802398403948_내사진.jpg
                String type = files.getContentType();
                Integer fileSize = Long.valueOf(files.getSize()).intValue();
//            String savePath = "/Users/사용자이름/springboot_img/" + storedFileName; // C:/springboot_img/9802398403948_내사진.jpg
                files.transferTo(new File(filePath)); // 5.

                petsitterFile = PetsitterFile.builder()
                        .originFileName(originFileName)
                        .newFileName(newFileName)
                        .filePath(filePath)
                        .type(type)
                        .fileSize(fileSize)
                        .build();

                petsitterFile.setPetsitter(board);

                //petsitterFileRepository.save(petsitterFile).setFileNo(saveId); //펫시터파일 테이블에 내가 작성한(메인게시글) 글번호를 넣겠다
                petsitterFileRepository.save(petsitterFile);
            }
            petsitterRepository.save(petsitter);

        }

    }
}
