package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
@SessionAttributes({"loginMember"})
public class MemberController {

    @Autowired
    private MemberService memberService;

    
    /**
     * 로그인
     * @param inputMember   로그인 정보
     * @param ra            리다이렉트 시 데이터 전달
     * @param model         데이터 전달
     * @return 로그인 결과 페이지 또는 메인 페이지
     */

    @PostMapping("login")
    public String login(Member inputMember,
                    RedirectAttributes ra,
                    Model model,
                    @RequestParam(value="saveId", required=false) String saveId,
                    HttpServletResponse resp) {

        Member loginMember = memberService.login(inputMember);
        
        if(loginMember == null) {
            // 로그인 실패
            ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
        } else {
            // 로그인 성공
            model.addAttribute("loginMember", loginMember);

            Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
            cookie.setPath("/");

            if(saveId != null) {
                cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
            } else {
                cookie.setMaxAge(0); // 쿠키 삭제
            }
            resp.addCookie(cookie);
        }

        return "redirect:/";
    }

    /**
     * 로그아웃
     * @param session
     * @return 메인 페이지
     */
    @GetMapping("logout")
    public String logout(SessionStatus status) {
        status.setComplete();
            return "redirect:/";
    }
    
// ↑ 세션 상태 완료 / ↓ 세션 무효화

    // public String logout(HttpSession session) {
    //     session.invalidate();
    //     return "redirect:/";
    // }

    /**
     * 회원 가입 페이지 요청
     * @return 회원 가입 페이지
     */
    @GetMapping("signup")
    public String signup() {
        return "member/signup";
    }

    /**
     * 회원 가입 서비스 요청
     * @param member
     * @return 메인 페이지
     */
    @PostMapping("signup")
    public String signup(Member inputMember,
                        @RequestParam("memberAddress") String[] memberAddress,
                        RedirectAttributes ra) {
        int result = memberService.signup(inputMember, memberAddress);
        String message;
        String path;
        
        if(result > 0) {
            message = inputMember.getMemberNickname() + "님의 가입을 환영합니다.";
            path = "/";
        } else {
            message = "회원 가입 실패";
            path = "signup";
        }

        ra.addFlashAttribute("message", message);
        return "redirect:"+path;
    }

    
    /**
     * 이메일 중복 검사
     * @param memberEmail
     * @return 사용 가능한 이메일 여부
     */
    @ResponseBody
    @GetMapping("checkEmail")
    public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
        return memberService.checkEmail(memberEmail);
    }

    /**
     * 닉네임 중복 검사
     * @param memberNickname
     * @return 사용 가능한 닉네임 여부
     */
    @ResponseBody
    @GetMapping("checkNickname")
    public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
        return memberService.checkNickname(memberNickname);
    }


    /**
     * ID 찾기 페이지 요청
     * @return ID 찾기 페이지
     */
    @GetMapping("find")
    public String find() {
        return "member/find";
    }

    // @PostMapping("findId")
    // @ResponseBody
    // public String findId(@RequestBody Member inputMember) {
    //     return memberService.findId(inputMember);
    // }

}
