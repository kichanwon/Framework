package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    public Member login(Member inputMember) {
        
        // 암호화 처리
        /*
         * bcrypt.encode(비밀번호) : 암호화 처리
         * bcrypt.matches(비밀번호, 암호화된 비밀번호) : 암호화된 비밀번호 비교
         * 
         * String encPw = bcrypt.encode(inputMember.getMemberPw());
         */
        Member loginMember = memberMapper.login(inputMember.getMemberEmail());

        if(loginMember == null) return null;

        if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
            return null;
        }

        loginMember.setMemberPw(null);

        return loginMember;
    }

    @Override
    public int checkEmail(String memberEmail) {
        return memberMapper.checkEmail(memberEmail);
    }

    @Override
    public int checkNickname(String memberNickname) {
        return memberMapper.checkNickname(memberNickname);
    }
    
    @Override
    public int signup(Member inputMember, String[] memberAddress) {
        // 비밀번호 암호화
        String encPw = bcrypt.encode(inputMember.getMemberPw());
        inputMember.setMemberPw(encPw);

        // 주소가 있으면 주소 문자열을 하나의 문자열로 합치기
        if(!inputMember.getMemberAddress().equals(",,")) {
            String address = String.join("^^^", memberAddress);
            inputMember.setMemberAddress(address);
        } else {
            inputMember.setMemberAddress(null);
        }

        return memberMapper.signup(inputMember);
    }
}
