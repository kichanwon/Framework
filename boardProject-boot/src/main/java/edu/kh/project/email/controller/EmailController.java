package edu.kh.project.email.controller;

// import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.email.model.service.EmailService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {

    // @Autowired
    private final EmailService emailService;
    
    @ResponseBody
    @PostMapping("signup")
    public int signup(@RequestBody String memberEmail) {
        String authKey = emailService.sendEmail("signup",memberEmail);
        
        if(authKey != null) return 1;
        return 0;
    }

    @ResponseBody
    @PostMapping("checkAuthKey")
    public int checkAuthKey(@RequestBody Map<String, String> map) {
        return emailService.checkAuthKey(map);
    }

}
