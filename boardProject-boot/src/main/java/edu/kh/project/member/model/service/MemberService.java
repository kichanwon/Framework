package edu.kh.project.member.model.service;

import org.springframework.stereotype.Service;

import edu.kh.project.member.model.dto.Member;

@Service
public interface MemberService {

    // 로그인
    public Member login(Member inputMember);

    // 이메일 중복 검사
    public int checkEmail(String memberEmail);

    // 닉네임 중복 검사
    public int checkNickname(String memberNickname);

    // 회원 가입
    public int signup(Member inputMember, String[] memberAddress);
    
}
