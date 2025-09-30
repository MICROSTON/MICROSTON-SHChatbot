package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.Localinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalinfoRepository extends JpaRepository<Localinfo, Integer> {
}
