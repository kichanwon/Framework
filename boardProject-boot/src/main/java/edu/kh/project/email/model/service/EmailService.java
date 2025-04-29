package edu.kh.project.email.model.service;

import java.util.Map;

import org.springframework.stereotype.Service;  

@Service
public interface EmailService {

    /**
     * 이메일 발송
     * @param type 이메일 타입
     * @param email 이메일
     * @return 인증키
     */
    String sendEmail(String type, String email);

    /**
     * 인증키 확인
     * @param map 이메일, 인증키
     * @return 인증키 확인 결과
     */
    int checkAuthKey(Map<String, String> map);
}
