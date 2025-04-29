package edu.kh.project.email.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;  

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor   // final field dependency injection
public class EmailServiceImpl implements EmailService {

    @Autowired
    private /*final*/ EmailMapper emailMapper;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public String sendEmail(String type, String email) {

        String authKey = createAuthKey();
        log.debug("authKey : {}", authKey);

        Map<String, String> map = new HashMap<>();
        map.put("authKey", authKey);
        map.put("email", email);

        if(!storeAuthKey(map)) {
            return null;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[boardProject] 이메일 인증 키 발송");
            helper.setText(loadHTML(type, authKey), true);
            
            helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
            
            javaMailSender.send(message);
            
            return authKey;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
            
    private String loadHTML(String type, String authKey) {
        Context context = new Context();
        context.setVariable("authKey", authKey);

        return templateEngine.process("email/" + type, context);
    }

    @Transactional(rollbackFor = Exception.class)
    private boolean storeAuthKey(Map<String, String> map) {
        int result = emailMapper.updateAuthKey(map);

        if(result == 0) {
            result = emailMapper.insertAuthKey(map);
        }

        return result > 0;
    }

    private String createAuthKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    @Override
    public int checkAuthKey(Map<String, String> map) {
        return emailMapper.checkAuthKey(map);
    }

}
