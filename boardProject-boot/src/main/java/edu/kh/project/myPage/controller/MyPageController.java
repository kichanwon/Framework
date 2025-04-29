package edu.kh.project.myPage.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.service.MyPageService;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
public class MyPageController {

    @Autowired
    private MyPageService service;

    @GetMapping("profile")
    public String profile() {
        return "myPage/myPage-profile";
    }

    /** 내 정보 조회 
     * @param loginMember : 로그인한 회원 정보
     * @param model : 모델
     * @return : 내 정보 수정 페이지
    */
    @GetMapping("info")
    public String info(@SessionAttribute("loginMember") Member loginMember
                        , Model model) {
        String memberAddress = loginMember.getMemberAddress();

        if(memberAddress != null) {
            String[] address = memberAddress.split("\\^\\^\\^");
            model.addAttribute("postcode", address[0]);
            model.addAttribute("address", address[1]);
            model.addAttribute("detailAddress", address[2]);
        }        

        return "myPage/myPage-info";
    }

    /** 내 정보 수정
     * @param member : 수정할 회원 정보             // 커맨드 객체(@ModelAttribute)
     * @param loginMember : 로그인한 회원 정보      // 세션 속성(@SessionAttribute)
     * @param memberAddress : 주소 배열            // 요청 속성(@RequestAttribute)
     * @param ra : 리다이렉트 속성  
     * @return : 내 정보 수정 페이지
     */
    @PostMapping("info")
    public String info(Member member,
                       @SessionAttribute("loginMember") Member loginMember,
                       @RequestParam("memberAddress") String[] memberAddress,
                       RedirectAttributes ra) {
        member.setMemberNo(loginMember.getMemberNo());

        int result = service.updateInfo(member, memberAddress);
        String message = null;

        if(result > 0) {
            loginMember.setMemberNickname(member.getMemberNickname());
            loginMember.setMemberTel(member.getMemberTel());
            loginMember.setMemberAddress(member.getMemberAddress());
            message = "회원 정보 수정 완료";
        } else {
            message = "회원 정보 수정 실패";
        }

        ra.addFlashAttribute("message", message);
        return "redirect:info";
    }

    /** 비밀번호 변경 페이지 요청
     * @return : 비밀번호 변경 페이지
    */
    @GetMapping("changePw")
    public String changePw() {
        return "myPage/myPage-changePw";
    }

    /** 비밀번호 변경
     * @param member : 변경할 비밀번호 정보
     * @param loginMember : 로그인한 회원 정보
     * @param ra : 리다이렉트 속성
     * @return : 비밀번호 변경 페이지
     */
    @PostMapping("changePw")
    public String changePw(@RequestParam Map<String, String> paramMap,
                          @SessionAttribute("loginMember") Member loginMember,
                          RedirectAttributes ra) {

        int result = service.changePw(paramMap, loginMember.getMemberNo());
        
        String message;
        String path;

        if(result > 0) {
            message = "비밀번호 변경 완료";
            path = "redirect:/";
        } else {
            message = "비밀번호 변경 실패";
            path = "redirect:changePw";
        }

        ra.addFlashAttribute("message", message);
        return path;
    }
    
    /** 회원 탈퇴 페이지 요청
     * @return : 회원 탈퇴 페이지
     */
    @GetMapping("secession")
    public String secession() {
        return "myPage/myPage-secession";
    }

    /** 회원 탈퇴
     * @param paramMap : 회원 탈퇴 정보
     * @param loginMember : 로그인한 회원 정보
     * @param ra : 리다이렉트 속성
     * @param status : 세션 상태 → @SessionAttributes 작성 필요!! 
     * @return : 회원 탈퇴 페이지
     */
    @PostMapping("secession")
    public String secession(@RequestParam("memberPw") String memberPw,
                           @SessionAttribute("loginMember") Member loginMember,
                           RedirectAttributes ra,
                           SessionStatus status) {

        int result = service.secession(memberPw, loginMember.getMemberNo());

        String message;
        String path;

        if(result > 0) {
            status.setComplete();
            message = "탈퇴 완료";
            path = "redirect:/";
        } else {
            message = "비밀번호가 일치하지 않습니다.";
            path = "redirect:secession";
        }
        ra.addFlashAttribute("message", message);
        return path;
    }

    @GetMapping("fileTest")
    public String fileTest() {
        return "myPage/myPage-fileTest";
    }

    @PostMapping("file/test1")
    public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile
                            , RedirectAttributes ra) throws Exception {

        String path = service.fileUpload1(uploadFile);
        
        if(path!=null) {
            ra.addFlashAttribute("path", path);
        }

        return "redirect:/myPage/fileTest";
    }
    @PostMapping("file/test2")
    public String fileUpload2(@RequestParam("uploadFile") MultipartFile uploadFile,
                             RedirectAttributes ra,
                             @SessionAttribute("loginMember") Member loginMember) throws Exception {

        int result = service.fileUpload2(uploadFile, loginMember.getMemberNo());
        
        String message;

        if(result > 0) {
            message = "업로드 완료";
        } else {
            message = "업로드 실패";
        }
        ra.addFlashAttribute("message", message);

        return "redirect:/myPage/fileTest";
    }

    
    @GetMapping("fileList")
    public String fileList(Model model,
                          @SessionAttribute("loginMember") Member loginMember) {

        int memberNo = loginMember.getMemberNo();
        List<UploadFile> list = service.selectFileList(memberNo);

        model.addAttribute("list", list);

        return "myPage/myPage-fileList";
    }

    @PostMapping("file/test3")
    public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList,
                             @RequestParam("bbb") List<MultipartFile> bbbList,
                             RedirectAttributes ra,
                             @SessionAttribute("loginMember") Member loginMember) throws Exception {

        int result = service.fileUpload3(aaaList, bbbList, loginMember.getMemberNo());
        
        String message;

        if(result > 0) {
            message = result + "개의 파일 업로드 완료";
        } else {
            message = "업로드 된 파일이 없습니다.";
        }
        ra.addFlashAttribute("message", message);

        return "redirect:/myPage/fileTest";
    }

    @PostMapping("profile")
    public String profile(@RequestParam("profileImg") MultipartFile profileImg,
                          @SessionAttribute("loginMember") Member loginMember,
                          RedirectAttributes ra) throws Exception {

        int result = service.profile(profileImg, loginMember);

        String message;

        if(result > 0) {
            message = "프로필 이미지 수정 완료";
        } else {
            message = "프로필 이미지 수정 실패";
        }

        ra.addFlashAttribute("message", message);
        return "redirect:profile";
    }
}
