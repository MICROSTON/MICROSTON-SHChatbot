package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.Benefit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    List<Benefit> findByAgeGroup_AgeGroupNumAndBenefitCategory_num(int ageGroupNum, Long categoryNum);
    List<Benefit> findByAgeGroup_AgeGroupNumAndBenefitCategory_num(int ageGroupNum, Long categoryNum, Pageable pageable);
}
