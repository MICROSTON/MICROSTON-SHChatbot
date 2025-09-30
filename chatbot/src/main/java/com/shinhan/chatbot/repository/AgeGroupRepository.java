package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeGroupRepository extends JpaRepository<AgeGroup, Integer> {
}
