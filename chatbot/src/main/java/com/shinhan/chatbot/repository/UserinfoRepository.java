package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserinfoRepository extends JpaRepository<Userinfo, String> {

    Optional<Userinfo> findById(String id);


    @Query("SELECT u.id FROM Userinfo u WHERE u.name = :name AND u.phone = :phone")
    Optional<String> findUserIdByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

    @Query("SELECT u.pw FROM Userinfo u WHERE u.id = :id AND u.name = :name AND u.phone = :phone")
    Optional<String> findPasswordByIdNamePhone(@Param("id") String id, @Param("name") String name, @Param("phone") String phone);
}
