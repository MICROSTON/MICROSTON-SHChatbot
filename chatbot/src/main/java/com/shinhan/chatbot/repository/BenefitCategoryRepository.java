package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.BenefitCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitCategoryRepository extends JpaRepository<BenefitCategory, Long> {
}
