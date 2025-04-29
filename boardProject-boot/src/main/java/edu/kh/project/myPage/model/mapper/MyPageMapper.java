package edu.kh.project.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

    /** 내 정보 수정
     * @param member
     */
    int updateInfo(Member member);

    /** 비밀번호 조회
     * @param memberNo
     * @return
     */
    String selectPw(int memberNo);

    /** 비밀번호 변경
     * @param paramMap
     * @return
     */
    int changePw(Map<String, String> paramMap);

    /** 회원 탈퇴
     * @param memberNo
     * @return
     */
    int secession(int memberNo);

    /** 파일 업로드
     * @param uf
     * @return
     */
    int insertUploadFile(UploadFile uf);

    /** 사용자 파일 목록 조회
     * @param memberNo
     * @return
     */
    List<UploadFile> selectFileList(int memberNo);

    /** 프로필 이미지 수정
     * @param member
     * @return 수정된 행의 개수
     */
    int profile(Member member);

}
