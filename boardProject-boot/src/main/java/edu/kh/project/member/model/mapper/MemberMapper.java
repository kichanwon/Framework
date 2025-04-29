package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

    public Member login(String memberEmail);

    public int checkEmail(String memberEmail);

    public int checkNickname(String memberNickname);

    public int signup(Member member);

}
