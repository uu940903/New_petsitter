package com.pet.sitter.mainboard.service;

import com.pet.sitter.common.entity.Member;
import com.pet.sitter.common.entity.Petsitter;
import com.pet.sitter.common.entity.PetsitterFile;
import com.pet.sitter.common.entity.Week;
import com.pet.sitter.exception.DataNotFoundException;
import com.pet.sitter.mainboard.dto.PetSitterDTO;
import com.pet.sitter.mainboard.dto.PetSitterFileDTO;
import com.pet.sitter.mainboard.dto.WeekDTO;
import com.pet.sitter.mainboard.repository.PetsitterFileRepository;
import com.pet.sitter.mainboard.repository.PetsitterRepository;
import com.pet.sitter.mainboard.repository.PetsitterSpec;
import com.pet.sitter.mainboard.repository.WeekRepository;
import com.pet.sitter.mainboard.validation.WriteForm;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
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
public class MainBoardService {

    private final Logger logger = LoggerFactory.getLogger(MainBoardService.class);

    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PetsitterFileRepository petsitterFileRepository;

    @Autowired
    private WeekRepository weekRepository;


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

    //게시글 목록 조회 - 회원
    public Page<PetSitterDTO> getListByMember(String name, int page) {
        Optional<Member> memberOptional = memberRepository.findBymemberId(name);
        if(memberOptional.isEmpty()){
            throw new DataNotFoundException("없는 회원이에요.");
        }
        Member member = memberOptional.get();
        String memberAddress = member.getAddress().substring(0, 2);
        System.out.println("memberAddress(회원 지역) = "+memberAddress);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("petViewCnt"));
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<Petsitter> petsitterPage = petsitterRepository.findAllByPetAddressContaining(pageable, memberAddress);
        System.out.println(petsitterPage.getTotalPages());
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

    @Transactional
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
            case "weekday":
                dayList.add("월");
                dayList.add("화");
                dayList.add("수");
                dayList.add("목");
                dayList.add("금");
                break;
            case "weekend":
                dayList.add("토");
                dayList.add("일");
                break;
            case "allDay":
                dayList = null;
                break;
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

        Page<Petsitter> petsitterPage = petsitterRepository.findAll(PetsitterSpec.searchWith(category, petCategory, petAddress, dayList, startTimeHour, endTimeHour), pageable);
        Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            List<PetSitterFileDTO> petSitterFileDTOList = petsitter.getPetsitterFileList().stream()
                    .map(petsitterFile -> PetSitterFileDTO.builder()
                            .petsitterFile(petsitterFile)
                            .build())
                    .collect(Collectors.toList());
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            List<WeekDTO> weekDTOList = petsitter.getWeekList().stream()
                    .map(week -> WeekDTO.builder()
                            .week(week)
                            .build())
                    .collect(Collectors.toList());
            petSitterDTO.setWeekDTOList(weekDTOList);
            return petSitterDTO;
        });
        return petSitterDTOPage;
    }

    //recommend
    public Page<PetSitterDTO> recommendList(String category, String petCategory, String sitterAddress) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(0, 4, Sort.by(sorts));
        Page<Petsitter> petsitterPage = petsitterRepository.findAll(PetsitterSpec.recommendWith(category, petCategory, sitterAddress), pageable);
        Page<PetSitterDTO> petSitterDTOPage = petsitterPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            List<PetSitterFileDTO> petSitterFileDTOList = petsitter.getPetsitterFileList().stream()
                    .map(petsitterFile -> PetSitterFileDTO.builder()
                            .petsitterFile(petsitterFile)
                            .build())
                    .collect(Collectors.toList());
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            List<WeekDTO> weekDTOList = petsitter.getWeekList().stream()
                    .map(week -> WeekDTO.builder()
                            .week(week)
                            .build())
                    .collect(Collectors.toList());
            petSitterDTO.setWeekDTOList(weekDTOList);
            return petSitterDTO;
        });
        return petSitterDTOPage;
    }


    //혜지시작
    //AreaSearch 테이블에 먼저 insert
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


        Long saveId = petsitterRepository.save(petsitter).getSitterNo();
        Petsitter board = petsitterRepository.findById(saveId).get();

        // 요일 데이터 저장 로직
        List<Week> weekList = new ArrayList<>();
        for (WeekDTO weekDTO : petSitterDTO.getWeekDTOList()) {
            Week week = new Week();
            week.setPetsitter(board);
            week.setDay(weekDTO.getDay());
            weekList.add(week);
            weekRepository.save(week);
        }

        if (boardFile[0].isEmpty()) { //파일이 없을 때
            petsitterRepository.save(petsitter);
        } else { //파일이 있을 때
            PetsitterFile petsitterFile = new PetsitterFile();

            String uploadDirectory = "C:/uploadfile/petsitter_img/";
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile files : boardFile) {
                String originFileName = files.getOriginalFilename();
                String newFileName = System.currentTimeMillis() + "_" + originFileName;
                String filePath = uploadDirectory + newFileName;
                String type = files.getContentType();
                Integer fileSize = Long.valueOf(files.getSize()).intValue();
                files.transferTo(new File(filePath));

                petsitterFile = PetsitterFile.builder()
                        .originFileName(originFileName)
                        .newFileName(newFileName)
                        .filePath(filePath)
                        .type(type)
                        .fileSize(fileSize)
                        .build();

                petsitterFile.setPetsitter(board);
                petsitterFileRepository.save(petsitterFile);
            }
            petsitterRepository.save(petsitter);

        }

    }

    //게시글 수정
    public void update(WriteForm writeForm, Long sitterNo,MultipartFile[] boardFile, String id) throws IOException {
        logger.info("MainBoardService-update()진입");

        Petsitter existingPost = petsitterRepository.findBySitterNo(sitterNo);

        if (existingPost == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        //로그인한 유저정보 받아옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<Member> memberOptional = memberRepository.findBymemberId(id);
        Member member = new Member();
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
        }

        // id 정보 담아주고, MySQL에서 기본값 설정이 JPA에선 안먹어서 다시 설정해줌
        logger.info("Member 정보 : {}", member);
        existingPost.setMember(member);
        existingPost.setLikeCnt(0);
        existingPost.setPetRegdate(LocalDateTime.now());
        existingPost.setPetViewCnt(0);

        if (existingPost.getPrice() == null) {
            existingPost.setPrice(0);
        }

        Petsitter petsitter = new Petsitter();
        PetsitterFile petsitterFile = new PetsitterFile();
        //existingPost.setPetsitterFileList(petsitterFile.getPetsitter().getPetsitterFileList());
        existingPost.setPetTitle(writeForm.getPetTitle());
        existingPost.setPetCategory(writeForm.getPetCategory());
        existingPost.setCategory(writeForm.getCategory());
        existingPost.setPrice(writeForm.getPrice());
        existingPost.setStartTime(writeForm.getStartTime());
        existingPost.setEndTime(writeForm.getEndTime());
        existingPost.setPetContent(writeForm.getPetContent());
        existingPost.setPetAddress(writeForm.getPetAddress());
        //우편 API사용하는데, 우리가 필요로하지 않은 정보들까지 있어서 문자열 자름
        String address = existingPost.getPetAddress();
        int idx = address.indexOf(",");

        String petAddress = address.substring(0, idx);

        existingPost.setPetAddress(petAddress);

        if (boardFile[0].isEmpty()) { //파일이 없을 때
            petsitterRepository.save(existingPost);
        } else { //파일이 있을 때

            Long saveId = petsitterRepository.save(petsitter).getSitterNo(); //현재 입력한 글번호를 받아오는 코드
            Petsitter board = petsitterRepository.findById(saveId).get();

            petsitterFile = new PetsitterFile();

            String uploadDirectory = "C:/uploadfile/petsitter_img/";

            File directory = new File(uploadDirectory);

            if (!directory.exists()) {
                directory.mkdirs(); // 폴더를 생성합니다.
            }

            for (MultipartFile files : boardFile) {
                String originFileName = files.getOriginalFilename();
                String newFileName = System.currentTimeMillis() + "_" + originFileName;
                String filePath = uploadDirectory + newFileName;
                String type = files.getContentType();
                Integer fileSize = Long.valueOf(files.getSize()).intValue();
                files.transferTo(new File(filePath));

                petsitterFile = PetsitterFile.builder()
                        .originFileName(originFileName)
                        .newFileName(newFileName)
                        .filePath(filePath)
                        .type(type)
                        .fileSize(fileSize)
                        .build();

                petsitterFile.setPetsitter(board);

                petsitterFileRepository.save(petsitterFile);
            }
            //petsitterRepository.save(petsitter);
        }
    }


    //게시글 삭제
    public void delete (Long sitterNo, String id) {
        logger.info("MainBoardService-delete() 진입");

        Petsitter existingPost = petsitterRepository.findBySitterNo(sitterNo);

        if (existingPost == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }


        // 게시글 작성자와 로그인한 유저가 동일한지 확인
        if (!existingPost.getMember().getMemberId().equals(id)) {
            throw new DataNotFoundException("삭제 권한이 없습니다."); //권한 예외처리로 다시 잡아주기
        }

        // 게시글에 연결된 파일 업로드용 테이블 데이터를 삭제
        List<PetsitterFile> filesToDelete = petsitterFileRepository.findByPetsitter(existingPost);
        petsitterFileRepository.deleteAll(filesToDelete);

        // 게시글 데이터 삭제
        petsitterRepository.delete(existingPost);
    }

    @Transactional
    //게시글 조회수 증가
    public int updateViews (Long no) {
        return petsitterRepository.updateViews(no);
    }

    @Transactional
    //추천수 증가
    public int incrementLikes (Long no) {
        return petsitterRepository.updateLike(no);
    }


    @Transactional
    public Page<PetSitterDTO> titleSearch(String keyword,int page) {
        List<Sort.Order> sorts = new ArrayList<>();

        sorts.add(Sort.Order.desc("petRegdate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        Page<Petsitter> titleSearchPage  = petsitterRepository.findByPetTitleContaining(pageable,keyword);
        Page<PetSitterDTO> petSitterDTOPage = titleSearchPage.map(petsitter -> {
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            List<PetSitterFileDTO> petSitterFileDTOList = petsitter.getPetsitterFileList().stream()
                    .map(petsitterFile -> PetSitterFileDTO.builder()
                            .petsitterFile(petsitterFile)
                            .build())
                    .collect(Collectors.toList());
            petSitterDTO.setFileDTOList(petSitterFileDTOList);
            List<WeekDTO> weekDTOList = petsitter.getWeekList().stream()
                    .map(week -> WeekDTO.builder()
                            .week(week)
                            .build())
                    .collect(Collectors.toList());
            petSitterDTO.setWeekDTOList(weekDTOList);
            return petSitterDTO;
        });
        return petSitterDTOPage;
    }
}

