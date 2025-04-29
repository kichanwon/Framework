package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private int memberNo;           // 회원 번호
    private String memberEmail;     // 이메일
    private String memberPw;        // 비밀번호
    private String memberNickname;  // 닉네임
    private String memberTel;       // 전화번호
    private String memberAddress;   // 주소
    private String profileImg;       // 프로필 이미지 경로
    private String enrollDate;      // 가입일   
    private String memberDelFl;     // 탈퇴여부 (Y/N)
    private String authority;       // 권한 (USER/ADMIN)
}
