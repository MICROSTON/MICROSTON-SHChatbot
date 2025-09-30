package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.Good;
import com.shinhan.chatbot.DB.GoodId;
import com.shinhan.chatbot.DB.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodRepository extends JpaRepository<Good, GoodId> {
    List<Good> findByUserinfo(Userinfo userinfo);
}


