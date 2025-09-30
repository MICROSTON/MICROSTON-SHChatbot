package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserinfoRepository extends JpaRepository<Userinfo, Long> {

    // 로그인 아이디 중복 확인용 - 락 없이 존재 여부만 확인
    boolean existsById(String id);

    // 로그인 아이디로 사용자 조회
    Optional<Userinfo> findById(String id);

    // 이름/전화번호로 아이디 찾기
    @Query("SELECT u.id FROM Userinfo u WHERE u.name = :name AND u.phone = :phone")
    Optional<String> findIdByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

    // 아이디/전화번호로 비밀번호 찾기
    @Query("SELECT u.pw FROM Userinfo u WHERE u.id = :id AND u.phone = :phone")
    Optional<String> findPasswordByIdAndPhone(@Param("id") String id, @Param("phone") String phone);
}
