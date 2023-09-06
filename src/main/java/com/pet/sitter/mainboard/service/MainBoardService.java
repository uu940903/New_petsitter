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
import com.pet.sitter.mainboard.repository.WeekRepository;
import com.pet.sitter.mainboard.validation.WriteForm;
import com.pet.sitter.member.dto.MemberDTO;
import com.pet.sitter.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<PetSitterDTO> getList() {
        List<Petsitter> petsitterList = petsitterRepository.findAll();
        List<PetSitterDTO> petSitterDTOList = new ArrayList<>();
        for (int i = 0; i < petsitterList.size(); i++) {
            Petsitter petsitter = petsitterList.get(i);
            PetSitterDTO petSitterDTO = PetSitterDTO.builder().petsitter(petsitter).build();
            petSitterDTO.setMember(MemberDTO.builder().member(petsitter.getMember()).build());

            /*
            PetSitterFileDTO petSitterFileDTO = PetSitterFileDTO.builder().petsitterFile(petsitterList.get(i).getPetsitterFileList().get(i)).build();
            System.out.println("petSitterFileDTO = "+petSitterFileDTO);
            petSitterFileDTOList.add(i, PetSitterFileDTO.builder().petsitterFile(petsitterList.get(i).getPetsitterFileList().get(i)).build());
            petSitterDTO.setFileDTOList(petSitterFileDTOList);

            weekList.add(i, WeekDTO.builder().week(petsitterList.get(i).getWeekList().get(i)).build());
            System.out.println("weekList = "+weekList);
            petSitterDTO.setWeekDTOList(weekList);
             */
            petSitterDTOList.add(i, petSitterDTO);
        }
        return petSitterDTOList;
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

    //혜지시작
    //AreaSearch 테이블에 먼저 insert
   /* public WeekDTO insertWeek (WeekDTO weekDTO) {
        logger.info("MainBoardService-insertWeek()진입");

        Week week = Week.dtoToEntity(weekDTO);
        logger.info("MainBoardService-insertWeek() week: {}", week.getPetsitter());
        logger.info("MainBoardService-insertWeek() week: {}", week.getDay());

        weekRepository.save(week);

        logger.info("MainBoardService-insertWeek()종료");
        return weekDTO;
    }*/

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


 /*       // Week 엔티티 생성 및 연결
        Week week = new Week();
        week.setPetsitter(petsitter);
        week.setDay("수"); // 예시로 월요일 설정, 필요한 요일로 변경 가능

        List<Week> weekList = new ArrayList<>();
        weekList.add(week);
        petsitter.setWeekList(weekList);

        // Week 엔티티를 저장
        weekRepository.save(week);*/



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

            //Petsitter 구한 번호를 PetsitterFile이랑 연결시켜줘야지


            petsitterFile = new PetsitterFile();

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


}

