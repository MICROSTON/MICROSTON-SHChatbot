package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.BookMarkId;

import com.shinhan.chatbot.DB.BookMark;
import com.shinhan.chatbot.DB.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, BookMarkId> {
    List<BookMark> findByUserinfo(Userinfo user);
}
