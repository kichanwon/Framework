package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

    /**
     * 인증키 업데이트
     * @param map
     * @return
     */
    int updateAuthKey(Map<String, String> map);

    /**
     * 인증키 삽입
     * @param map
     * @return
     */
    int insertAuthKey(Map<String, String> map);

    /**
     * 인증키 확인
     * @param map
     * @return
     */
    int checkAuthKey(Map<String, String> map);

}
