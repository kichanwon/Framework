package edu.kh.project.myPage.model.service;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;

@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
public class MyPageServiceImple implements MyPageService{

    @Autowired
    private MyPageMapper mapper;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Value("${my.profile.folder-path}")
    private String profileFolderPath;

    @Value("${my.profile.web-path}")
    private String profileWebPath;




    @Override
    public int updateInfo(Member member, String[] memberAddress) {
        if(!member.getMemberAddress().equals(",,")) {
            String address = String.join("^^^", memberAddress);
            member.setMemberAddress(address);
        } else {
            member.setMemberAddress(null);
        }
        
        return mapper.updateInfo(member);
    }

    @Override
    public int changePw(Map<String, String> paramMap, int memberNo) {

        // 1. 현재 비밀번호 일치 여부 확인
        String encPw = mapper.selectPw(memberNo);
        if(!bcrypt.matches(paramMap.get("currentPw"), encPw)) {
            return 0;
        }

        // 2. 비밀번호 암호화
        String encNewPw = bcrypt.encode(paramMap.get("newPw"));
        paramMap.put("encNewPw", encNewPw);
        paramMap.put("memberNo", memberNo + "");

        return mapper.changePw(paramMap);
    }

    @Override
    public int secession(String memberPw, int memberNo) {

        String encPw = mapper.selectPw(memberNo);
        if(!bcrypt.matches(memberPw, encPw)) {
            return 0;
        }

        return mapper.secession(memberNo);
    }

    @Override
    public String fileUpload1(MultipartFile uploadFile) throws Exception {
        if(uploadFile.isEmpty()) return null;

        uploadFile.transferTo(new File("C:/uploadFiles/test/" + uploadFile.getOriginalFilename()));

        return "/myPage/file/" + uploadFile.getOriginalFilename();
    }

    @Override
    public int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception {
        if(uploadFile.isEmpty()) return 0;

        String folderPath = "C:/uploadFiles/test/";
        String webPath = "/myPage/file/";

        String rename = Utility.fileRename(uploadFile.getOriginalFilename());

        UploadFile uf = UploadFile.builder()
                        .memberNo(memberNo)
                        .filePath(webPath)
                        .fileOriginalName(uploadFile.getOriginalFilename())
                        .fileRename(rename)
                        .build();

        int result = mapper.insertUploadFile(uf);

        if(result == 0) return 0;

        uploadFile.transferTo(new File(folderPath + rename));

        return result;
    }

    @Override
    public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception {
        int result1 = 0;
        int result2 = 0;
        
        for(MultipartFile file : aaaList) {
            if(file.isEmpty()) continue;
            result1 += fileUpload2(file, memberNo);
        }

        for(MultipartFile file : bbbList) {
            if(file.isEmpty()) continue;
            result2 += fileUpload2(file, memberNo);
        }

        return result1 + result2;
    }

    @Override
    public List<UploadFile> selectFileList(int memberNo) {
        return mapper.selectFileList(memberNo);
    }

    @Override
    public int profile(MultipartFile profileImg, Member loginMember) throws Exception {
        if(profileImg.isEmpty()) return 0;

        String updatePath = null;
        String rename = null;

        if(!profileImg.isEmpty()) {
            rename = Utility.fileRename(profileImg.getOriginalFilename());
            updatePath = profileWebPath + rename;
        }

        Member member = Member.builder()
                        .memberNo(loginMember.getMemberNo())
                        .profileImg(updatePath)
                        .build();

        int result = mapper.profile(member);

        if(result > 0) {
            if(!profileImg.isEmpty()) {
                profileImg.transferTo(new File(profileFolderPath + rename));
            }
            loginMember.setProfileImg(updatePath);
        }

        return result;
    }
}
