package edu.kh.project.myPage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Service
public interface MyPageService {

    public int updateInfo(Member member, String[] memberAddress);

    public int changePw(Map<String, String> paramMap, int memberNo);

    public int secession(String memberPw, int memberNo);

    public String fileUpload1(MultipartFile uploadFile) throws Exception;

    public int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception;

    public List<UploadFile> selectFileList(int memberNo);

    public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception;

    public int profile(MultipartFile profileImg, Member loginMember) throws Exception;

}
